package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.loadbalancer.configuration.property.ScrapingProperties;
import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.ScrapingBatch;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.InitialDataRepository;
import com.dataox.loadbalancer.domain.repositories.ScrapingBatchRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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
    ScrapingBatchRepository scrapingBatchRepository;
    RabbitTemplate rabbitTemplate;
    DataLoaderService dataLoaderService;

    public void startInitialScraping(List<Long> denovoIds) {
        List<InitialData> initialData = initialDataRepository.findAllById(denovoIds);
        boolean isAllSearched = initialData.stream()
                .allMatch(InitialData::getSearched);
        if (isAllSearched) {
            startScraping(initialData);
        } else {
            triggerGoogleSearch();
        }

    }

    public void processScrapingResults(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinProfile> successfulProfiles = scrapingResultsDTO.getSuccessfulProfiles();
        dataLoaderService.saveLinkedinProfiles(successfulProfiles);
        List<String> profileUrls = successfulProfiles.stream()
                .map(LinkedinProfile::getProfileUrl)
                .collect(Collectors.toList());
        List<SearchResult> searchResults = searchResultRepository.findAllByUrlIn(profileUrls);
        ScrapingBatch scrapingBatch = scrapingBatchRepository.findBySearchResultsIn(searchResults);
        scrapingBatch.setFinished(true);
        scrapingBatch.setFinishedAt(Instant.now());
        scrapingBatchRepository.save(scrapingBatch);
        log.info("Received {} not scraped profiles", scrapingResultsDTO.getNotScrapedLinkedinProfiles().size());
    }

    private void startScraping(List<InitialData> initialData) {
        List<SearchResult> searchResults = searchResultRepository.findAllByInitialDataRecordIn(initialData);// в инишал дате уже есть серч резалты
        List<List<SearchResult>> resultBatches = splitSearchResults(searchResults);
        List<ScrapingBatch> scrapingBatches = createScrapingBatches(resultBatches);
        scrapingBatchRepository.saveAll(scrapingBatches);
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

    private List<ScrapingBatch> createScrapingBatches(List<List<SearchResult>> resultBatches) {
        List<ScrapingBatch> scrapingBatches = new ArrayList<>();
        for (List<SearchResult> resultBatch : resultBatches) {
            ScrapingBatch scrapingBatch = new ScrapingBatch();
            scrapingBatch.setSearchResults(resultBatch);
            resultBatch.forEach(searchResult -> searchResult.setScrapingBatch(scrapingBatch));
            scrapingBatches.add(scrapingBatch);
        }
        return scrapingBatches;
    }

    private List<List<SearchResult>> splitSearchResults(List<SearchResult> searchResults) {
        List<List<SearchResult>> searchResultBatches = new ArrayList<>();
        int profilesSize = searchResults.size();
        int batchSize = scrapingProperties.getBatchSize();
        for (int i = 0; i < profilesSize; i += batchSize) {
            searchResultBatches.add(searchResults.subList(i, Math.min(profilesSize, i + batchSize)));//Check ListUtils partition method
        }
        return searchResultBatches;
    }

    private void triggerGoogleSearch() {

    }
}
