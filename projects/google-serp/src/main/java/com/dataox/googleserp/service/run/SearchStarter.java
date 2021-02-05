package com.dataox.googleserp.service.run;

import com.dataox.googleserp.configuration.properties.SearchProperties;
import com.dataox.googleserp.exceptions.SearchException;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.repository.SearchResultRepository;
import com.dataox.googleserp.service.sending.SearchResultSender;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.dataox.googleserp.util.NotificationUtils.createErrorMessage;
import static com.dataox.googleserp.util.NotificationUtils.createSearchFailedMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchStarter {

    private final SearchResultSender searchResultSender;
    private final ExecutorService searchExecutorService;
    private final InitialDataRepository initialDataRepository;
    private final SearchResultRepository searchResultRepository;
    private final SearchProperties searchProperties;
    private final NotificationsService notificationsService;
    private static final String ALREADY_SEARCHED_ERROR_MESSAGE = "Can't start search with DenovoIds. Already searched";
    private static final String NO_DATA_TO_SEARCH_ERROR_MESSAGE = "Not searched data is not present in database!";

    public void startSearchWithDenovoIds(List<Long> denovoIds) {
        List<InitialData> initialData = initialDataRepository.findAllByDenovoIdInAndSearchedFalse(denovoIds);
        if (initialData.isEmpty()) {
            log.error(ALREADY_SEARCHED_ERROR_MESSAGE + " {}", denovoIds);
            throw new SearchException(ALREADY_SEARCHED_ERROR_MESSAGE);
        }
        CompletableFuture.runAsync(() -> {
            startSearchWithInitialData(initialData);
            List<Long> searchResultsIds = getResultsIdsByInitialData(initialData);
            searchResultSender.sendSearchResultIdsToLoadBalancer(searchResultsIds);
        }).exceptionally(throwable -> {
            notificationsService.sendInternal(createErrorMessage(throwable));
            return null;
        });
    }

    public void startSearchForNotSearchedInitialData() {
        List<InitialData> notSearchedInitialData = initialDataRepository.findAllBySearchedFalse();
        if (notSearchedInitialData.isEmpty()) {
            log.error(NO_DATA_TO_SEARCH_ERROR_MESSAGE);
            throw new SearchException(NO_DATA_TO_SEARCH_ERROR_MESSAGE);
        }
        CompletableFuture.runAsync(() -> startSearchWithInitialData(notSearchedInitialData))
                .exceptionally(throwable -> {
                    notificationsService.sendInternal(createErrorMessage(throwable));
                    return null;
                });
    }

    private synchronized void startSearchWithInitialData(List<InitialData> initialData) {
        List<Long> denovoIdsToSearch = extractDenovoIds(initialData);
        List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks = createSearchProfileWithGoogleTasks(initialData);

        log.info("Starting search for denovoIds: {}", denovoIdsToSearch);
        List<? extends Future<?>> futureList = submitTasks(searchProfileWithGoogleTasks);
        try {
            waitUntilTasksIsDone(searchProfileWithGoogleTasks, futureList);
            sendExceptionsInternalIfPresent(futureList);
        } catch (Exception e) {
            log.error("Failed to search for denovoIds: {}", denovoIdsToSearch);
            notificationsService.sendInternal(createSearchFailedMessage(e, denovoIdsToSearch));
        }
    }

    private List<? extends Future<?>> submitTasks(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks) {
        return searchProfileWithGoogleTasks.stream()
                .map(searchExecutorService::submit)
                .collect(Collectors.toList());
    }

    private void waitUntilTasksIsDone(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks,
                                      List<? extends Future<?>> futureList) {
        int tasksAmount = Math.max(searchProfileWithGoogleTasks.size(), 100);
        long minutesToWait = (tasksAmount / searchProperties.getConcurrencyRestriction()) * searchProperties.getTasksTimeOut();
        try {
            Awaitility.await().atMost(minutesToWait, TimeUnit.MINUTES).until(() -> futureList.stream().allMatch(Future::isDone));
        } catch (Exception e) {
            futureList.forEach(future -> future.cancel(true));
            log.error("Canceled all tasks in the batch");
            throw e;
        }
    }

    private void sendExceptionsInternalIfPresent(List<? extends Future<?>> futureList) {
        List<Throwable> exceptions = collectExceptionsFromTasks(futureList);
        if (!exceptions.isEmpty())
            notificationsService.sendInternal(createErrorMessage(exceptions));
    }

    private List<Throwable> collectExceptionsFromTasks(List<? extends Future<?>> futureList) {
        Set<Class<? extends Throwable>> exceptionsClasses = new HashSet<>();
        List<Throwable> exceptions = new ArrayList<>();
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                if (exceptionsClasses.add(e.getCause().getClass()))
                    exceptions.add(e);
            }
        }
        return exceptions;
    }

    private List<SearchProfileWithGoogleTask> createSearchProfileWithGoogleTasks(List<InitialData> initialData) {
        return initialData.stream()
                .map(SearchProfileWithGoogleTask::new)
                .collect(Collectors.toList());
    }

    private List<Long> extractDenovoIds(List<InitialData> initialData) {
        return initialData.stream()
                .map(InitialData::getDenovoId)
                .collect(Collectors.toList());
    }

    private List<Long> getResultsIdsByInitialData(List<InitialData> initialData) {
        List<Long> initialDataIds = initialData.stream()
                .map(InitialData::getId)
                .collect(Collectors.toList());
        List<SearchResult> searchResults = searchResultRepository.findAllByInitialDataRecordIdIn(initialDataIds);
        return searchResults.stream()
                .map(SearchResult::getId)
                .collect(Collectors.toList());
    }
}
