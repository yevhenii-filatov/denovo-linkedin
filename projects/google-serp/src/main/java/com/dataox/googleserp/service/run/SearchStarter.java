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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.dataox.googleserp.util.NotificationUtils.createErrorMessage;

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

    public void startSearchWithDenovoIds(List<Long> denovoIds) {
        List<InitialData> initialData = initialDataRepository.findAllByDenovoIdInAndSearchedFalse(denovoIds);
        if (initialData.isEmpty()) {
            log.error("Can't start search with DenovoIds. Already searched: {}", denovoIds);
            throw new SearchException("Can't start search with DenovoIds. Already searched: {}");
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
            log.error("Not searched data is not present in database!");
            throw new SearchException("Not searched data is not present in database!");
        }
        log.info("Staring search for not searched initial data");
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
        waitUntilTasksIsDone(searchProfileWithGoogleTasks, futureList);
    }

    private List<? extends Future<?>> submitTasks(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks) {
        return searchProfileWithGoogleTasks.stream()
                .map(searchExecutorService::submit)
                .collect(Collectors.toList());
    }

    private void waitUntilTasksIsDone(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks,
                                      List<? extends Future<?>> futureList) {
        long minutesToWait = searchProfileWithGoogleTasks.size() * searchProperties.getMinutesForOneTask();
        try {
            Awaitility.await().atMost(minutesToWait, TimeUnit.MINUTES).until(() -> futureList.stream().allMatch(Future::isDone));
        } catch (Exception e) {
//            futureList.forEach(future -> future.cancel(false));
//            log.error("Canceled all tasks in the batch");
            throw e;
        }
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
