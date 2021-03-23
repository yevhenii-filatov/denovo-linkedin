package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.NotScrapedLinkedinProfile;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.loadbalancer.configuration.property.ScrapingProperties;
import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.InitialDataRepository;
import com.dataox.loadbalancer.domain.repositories.LinkedinNotReusableProfileRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import com.dataox.loadbalancer.exception.DataNotFoundException;
import com.dataox.loadbalancer.exception.RecordNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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
    LinkedinNotReusableProfileRepository notReusableProfileRepository;
    RabbitTemplate rabbitTemplate;
    DataLoaderService dataLoaderService;

    public void startInitialScraping(List<Long> denovoIds) {
        List<InitialData> initialData = initialDataRepository.findAllByDenovoIdIn(denovoIds);
        if (initialData.isEmpty())
            throw new DataNotFoundException("Initial data, with given denovo ids: " + denovoIds + " not found");
        List<InitialData> searchedAndFoundInitialData = initialData.stream()
                .filter(InitialData::getSearched)
                .filter(data -> !data.getNoResults())
                .collect(Collectors.toList());
        List<InitialData> notSearchedInitialData = initialData.stream()
                .filter(data -> !data.getSearched())
                .collect(Collectors.toList());
        if (!searchedAndFoundInitialData.isEmpty())
            startScraping(searchedAndFoundInitialData);
        if (!notSearchedInitialData.isEmpty())
            triggerGoogleSearch(notSearchedInitialData);

    }

    public void processScrapingResults(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinProfile> successfulProfiles = scrapingResultsDTO.getSuccessfulProfiles();
        dataLoaderService.saveLinkedinProfiles(successfulProfiles);
        processReusableNotScrapedProfiles(scrapingResultsDTO);
        processNotReusableProfiles(scrapingResultsDTO);
    }

    private void processNotReusableProfiles(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinNotReusableProfile> notReusableProfiles = convertResultsToNotReusableProfiles(scrapingResultsDTO);
        notReusableProfileRepository.saveAll(notReusableProfiles);
    }

    private List<LinkedinNotReusableProfile> convertResultsToNotReusableProfiles(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinNotReusableProfile> notReusableProfiles = new ArrayList<>();
        List<NotScrapedLinkedinProfile> notScrapedLinkedinProfiles = scrapingResultsDTO.getNotScrapedLinkedinProfiles().stream()
                .filter((profile -> !profile.isReusable()))
                .collect(Collectors.toList());
        for (NotScrapedLinkedinProfile notScrapedProfile : notScrapedLinkedinProfiles) {
            LinkedinProfileToScrapeDTO toScrapeDTO = notScrapedProfile.getProfileToScrapeDTO();
            LinkedinNotReusableProfile notReusableProfile = DTOConverter.toNotReusableProfile(toScrapeDTO);
            Long searchResultId = notScrapedProfile.getProfileToScrapeDTO().getSearchResultId();
            SearchResult searchResult = searchResultRepository.findById(searchResultId)
                    .orElseThrow(() -> new RecordNotFoundException("Search result with id " + searchResultId + " not found in database"));
            notReusableProfile.setSearchResult(searchResult);
            notReusableProfile.setErrorDescription(notScrapedProfile.getErrorDescription());
            notReusableProfiles.add(notReusableProfile);
        }
        return notReusableProfiles;
    }

    private void processReusableNotScrapedProfiles(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinProfileToScrapeDTO> profilesToScrape = scrapingResultsDTO.getNotScrapedLinkedinProfiles().stream()
                .filter(NotScrapedLinkedinProfile::isReusable)
                .map(NotScrapedLinkedinProfile::getProfileToScrapeDTO)
                .collect(Collectors.toList());
        List<List<LinkedinProfileToScrapeDTO>> splittedProfiles = ListUtils.partition(profilesToScrape, scrapingProperties.getBatchSize());
        if (!profilesToScrape.isEmpty())
            sendToQueue(splittedProfiles);
    }

    private void startScraping(List<InitialData> initialData) {
        List<SearchResult> searchResults = initialData.stream()
                .flatMap(initData -> initData.getSearchResults().stream())
                .collect(Collectors.toList());
        List<List<SearchResult>> resultBatches = ListUtils.partition(searchResults, scrapingProperties.getBatchSize());
        List<List<LinkedinProfileToScrapeDTO>> profileToScrapeBatchesLists = resultBatches.stream()
                .map(searchResult -> searchResult.stream()
                        .map(DTOConverter::profileToInitialScrapeDTO)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        sendToQueue(profileToScrapeBatchesLists);
    }

    private void sendToQueue(List<List<LinkedinProfileToScrapeDTO>> profileToScrapeBatchesLists) {
        profileToScrapeBatchesLists = profileToScrapeBatchesLists.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
        profileToScrapeBatchesLists.forEach(rabbitTemplate::convertAndSend);
        log.info("Sent {} batches to queue with batch size: {}", profileToScrapeBatchesLists.size(), scrapingProperties.getBatchSize());
    }

    private void triggerGoogleSearch(List<InitialData> notSearchedInitialData) {

    }
}
