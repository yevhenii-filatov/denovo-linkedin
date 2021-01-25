package com.dataox.googleserp.service.run;

import com.dataox.googleserp.configuration.properties.SearchProperties;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.repository.SearchResultRepository;
import com.dataox.googleserp.service.sending.SearchResultSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchStarter {

    private final SearchResultSender searchResultSender;
    private final ExecutorService searchExecutorService;
    private final InitialDataRepository initialDataRepository;
    private final SearchResultRepository searchResultRepository;
    private final SearchProperties searchProperties;

    public void startSearchWithInitialData(List<InitialData> initialData) {
        List<Long> denovoIdsToSearch = extractDenovoIds(initialData);
        List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks = createSearchProfileWithGoogleTasks(initialData);

        List<? extends Future<?>> futureList = submitTasks(searchProfileWithGoogleTasks);
        log.info("Started search for denovoIds: {}", denovoIdsToSearch);
        waitUntilTasksIsDone(searchProfileWithGoogleTasks, futureList);
        List<Long> searchResultsIds = getResultsIdsByInitialData(initialData);
        searchResultSender.sendSearchResultIdsToLoadBalancer(searchResultsIds);
    }

    public void startSearchWithDenovoIds(List<Long> denovoIds) {
        List<InitialData> initialData = initialDataRepository.findAllByDenovoIdInAndSearchedFalse(denovoIds);
        if (initialData.isEmpty()) {
            log.warn("Can't start search with DenovoIds. Already searched: {}", denovoIds);
            return;
        }
        startSearchWithInitialData(initialData);
    }

    public void startSearchForNotSearchedInitialData() {
        List<InitialData> notSearchedInitialData = initialDataRepository.findAllBySearchedFalse();
        if (notSearchedInitialData.isEmpty()) {
            log.warn("Not searched data is not present in database!");
            return;
        }
        log.info("Staring search for not searched initial data");
        startSearchWithInitialData(notSearchedInitialData);
    }

    private List<? extends Future<?>> submitTasks(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks) {
        return searchProfileWithGoogleTasks.stream()
                .map(searchExecutorService::submit)
                .collect(Collectors.toList());
    }

    private void waitUntilTasksIsDone(List<SearchProfileWithGoogleTask> searchProfileWithGoogleTasks,
                                      List<? extends Future<?>> futureList) {
        long minutesToWait = searchProfileWithGoogleTasks.size() * searchProperties.getMinutesForOneTask();
        Awaitility.await().atMost(minutesToWait, TimeUnit.MINUTES).until(() -> futureList.stream().allMatch(Future::isDone));
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
