package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.loadbalancer.configuration.property.ScrapingProperties;
import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.InitialDataRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScrapingService {

    ScrapingProperties scrapingProperties;
    InitialDataRepository initialDataRepository;
    SearchResultRepository searchResultRepository;
    RabbitTemplate rabbitTemplate;
    DataLoaderService dataLoaderService;

    public void startInitialScraping(List<Long> denovoIds) {//TODO add exception if initialData is empty
        List<InitialData> initialData = initialDataRepository.findAllById(denovoIds);
        List<InitialData> searchedAndFoundInitialData = initialData.stream()
                .filter(InitialData::getSearched)
                .filter(initialData1 -> !initialData1.getNoResults())
                .collect(Collectors.toList());
        List<InitialData> notSearchedInitialData = initialData.stream()
                .filter(initialData1 -> !initialData1.getSearched())
                .collect(Collectors.toList());
        if (!searchedAndFoundInitialData.isEmpty())
            startScraping(searchedAndFoundInitialData);
        if (!notSearchedInitialData.isEmpty())
            triggerGoogleSearch(notSearchedInitialData);

    }

    private void startScraping(List<InitialData> initialData) {
        List<SearchResult> searchResults = initialData.stream()
                .flatMap(initData -> initData.getSearchResults().stream())
                .collect(Collectors.toList());
        List<List<SearchResult>> resultBatches = ListUtils.partition(searchResults, scrapingProperties.getBatchSize());
        List<List<LinkedinProfileToScrapeDTO>> profileToScrapeBatchesLists = resultBatches.stream()
                .map(searchResults1 -> searchResults1.stream()
                        .map(DTOConverter::profileToInitialScrapeDTO)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        sendToQueue(profileToScrapeBatchesLists);
    }

    private void sendToQueue(List<List<LinkedinProfileToScrapeDTO>> profileToScrapeBatchesLists) {
        profileToScrapeBatchesLists.forEach(rabbitTemplate::convertAndSend);
        log.info("Sent {} batches to queue with batch size: {}", profileToScrapeBatchesLists.size(), scrapingProperties.getBatchSize());
    }

    private void triggerGoogleSearch(List<InitialData> notSearchedInitialData) {

    }
}
