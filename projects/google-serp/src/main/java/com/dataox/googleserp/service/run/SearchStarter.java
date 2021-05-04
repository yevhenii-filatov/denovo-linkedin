package com.dataox.googleserp.service.run;

import com.dataox.googleserp.configuration.properties.SearchProperties;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.service.ChooseBestApiTrigger;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.dataox.googleserp.util.NotificationUtils.createErrorMessage;
import static com.dataox.googleserp.util.NotificationUtils.createSearchFailedMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchStarter {

    private final ExecutorService searchExecutorService;
    private final InitialDataRepository initialDataRepository;
    private final ChooseBestApiTrigger chooseBestApiTrigger;
    private final SearchProperties searchProperties;
    private final NotificationsService notificationsService;

    public void startSearch(Map<InitialData, Integer> dataAndStep) {
        List<Long> denovoIds = extractDenovoIds(dataAndStep);
        try {
            CompletableFuture.runAsync(() -> {
                searchWithInitialData(dataAndStep);
                chooseBestApiTrigger.triggerAPI(denovoIds);
            }).exceptionally(throwable -> {
                notificationsService.sendInternal(createErrorMessage(throwable));
                return null;
            });
        } catch (Exception e) {
            notificationsService.sendInternal(createErrorMessage(e));
            throw e;
        }
    }

    private synchronized void searchWithInitialData(Map<InitialData, Integer> dataAndStep) {
        List<Long> denovoIdsToSearch = extractDenovoIds(dataAndStep);
        List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks = createSearchProfileWithGoogleTasks(dataAndStep);

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

    private List<SearchProfileWithGoogleTask> createSearchProfileWithGoogleTasks(Map<InitialData, Integer> initialDataSearchStepMap) {
        return initialDataSearchStepMap.entrySet().stream()
                .map(dataAndStep -> new SearchProfileWithGoogleTask(dataAndStep.getKey(), dataAndStep.getValue()))
                .collect(Collectors.toList());
    }

    private List<Long> extractDenovoIds(Map<InitialData, Integer> dataAndStep) {
        return dataAndStep.keySet().stream()
                .map(InitialData::getDenovoId)
                .collect(Collectors.toList());
    }
}
