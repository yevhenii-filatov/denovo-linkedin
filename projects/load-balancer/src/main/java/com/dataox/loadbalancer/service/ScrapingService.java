package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.NotScrapedLinkedinProfile;
import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.loadbalancer.configuration.property.ScrapingProperties;
import com.dataox.loadbalancer.domain.dto.InitialDataSearchPosition;
import com.dataox.loadbalancer.domain.dto.ScrapingDTO;
import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.domain.repositories.InitialDataRepository;
import com.dataox.loadbalancer.domain.repositories.LinkedinNotReusableProfileRepository;
import com.dataox.loadbalancer.domain.repositories.LinkedinProfileRepository;
import com.dataox.loadbalancer.domain.repositories.SearchResultRepository;
import com.dataox.loadbalancer.dto.LinkedinProfileToUpdateDTO;
import com.dataox.loadbalancer.exception.DataNotFoundException;
import com.dataox.loadbalancer.exception.RecordNotFoundException;
import com.dataox.notificationservice.service.NotificationsService;
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

import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScrapingService {

    NotificationsService notificationsService;
    ScrapingProperties scrapingProperties;
    InitialDataRepository initialDataRepository;
    SearchResultRepository searchResultRepository;
    LinkedinNotReusableProfileRepository notReusableProfileRepository;
    LinkedinProfileRepository linkedinProfileRepository;
    RabbitTemplate rabbitTemplate;
    DataLoaderService dataLoaderService;
    ScrapingDataValidationService dataValidationService;
    GoogleSearchService googleSearchService;

    public void startInitialScraping(List<ScrapingDTO> scrapingDTOS) {
        List<Long> denovoIds = getDenovoIds(scrapingDTOS);
        List<InitialDataSearchPosition> initialDataSearchPositions = getInitialDataSearchPositions(scrapingDTOS);
        List<InitialData> initialData = initialDataRepository.findAllByDenovoIdIn(denovoIds);
        dataValidationService.validateInitialData(denovoIds, initialData);
        List<InitialDataSearchPosition> searchedAndFoundInitialData = initialDataSearchPositions.stream()
                .filter(data -> data.getInitialData().getSearched())
                .filter(data -> !data.getInitialData().getNoResults())
//                .filter(data -> data.getInitialData().getSearchResults().size() == 1)
//                .filter(data -> data.getSearchPosition() == data.getInitialData().getSearchResults())
                .collect(Collectors.toList());
        List<InitialData> notSearchedInitialData = initialDataSearchPositions.stream()
                .map(InitialDataSearchPosition::getInitialData)
                .filter(data -> !data.getSearched())
                .collect(Collectors.toList());
        if (!searchedAndFoundInitialData.isEmpty())
            startScraping(searchedAndFoundInitialData);
        if (!notSearchedInitialData.isEmpty())
            log.info("denovoIds: {} doesn't present in InitialData table",
                    notSearchedInitialData.stream().map(InitialData::getDenovoId).collect(Collectors.toList()));
    }

    private List<InitialDataSearchPosition> getInitialDataSearchPositions(List<ScrapingDTO> scrapingDTOS) {
        return scrapingDTOS.stream()
                .map(dto -> {
                    InitialData initialData = initialDataRepository.findByDenovoId(dto.getDenovoId())
                            .orElseThrow(() -> new IllegalArgumentException(dto.getDenovoId() + " doesn't exists"));
//                    notificationsService.sendAll("LoadBalancer: DenovoId {"
//                                        .concat(Long.toString(dto.getDenovoId()))
//                                        .concat("} doesn't exists"));
                    return new InitialDataSearchPosition(initialData, dto.getSearchPosition());
                })
                .collect(Collectors.toList());
    }

    private List<Long> getDenovoIds(List<ScrapingDTO> scrapingDTOS) {
        return scrapingDTOS.stream().map(ScrapingDTO::getDenovoId).collect(Collectors.toList());
    }

    public void updateProfiles(List<LinkedinProfileToUpdateDTO> profileToUpdateDTOS) {
        List<Long> profileIds = profileToUpdateDTOS.stream()
                .map(LinkedinProfileToUpdateDTO::getLinkedinProfileId)
                .collect(Collectors.toList());
        List<LinkedinProfile> profilesToUpdate = linkedinProfileRepository.findAllById(profileIds);
        dataValidationService.validateUpdateProfiles(profilesToUpdate, profileIds);
        List<LinkedinProfileToScrapeDTO> profileToScrapeDTOS = DTOConverter.convertToProfileToScrapeDTOS(profileToUpdateDTOS, profilesToUpdate);
        List<List<LinkedinProfileToScrapeDTO>> splittedProfiles = ListUtils.partition(profileToScrapeDTOS, scrapingProperties.getBatchSize());
        sendToQueue(splittedProfiles);
    }

    public void rescrapeFixedProfiles(List<Long> notReusableProfileIds) {
        List<LinkedinNotReusableProfile> fixedProfiles = notReusableProfileRepository.findAllById(notReusableProfileIds);
        if (fixedProfiles.isEmpty())
            throw new DataNotFoundException("Not reusable profile records, with given ids " + notReusableProfileIds + " not found in database");
        List<LinkedinProfileToScrapeDTO> profileToScrapeDTOS = fixedProfiles.stream()
                .map(DTOConverter::toScrapeDTO)
                .collect(Collectors.toList());
        List<List<LinkedinProfileToScrapeDTO>> splittedProfiles = ListUtils.partition(profileToScrapeDTOS, scrapingProperties.getBatchSize());
        sendToQueue(splittedProfiles);
        notReusableProfileRepository.deleteAll(fixedProfiles);
    }

    public void processScrapingResults(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinProfile> successfulProfiles = scrapingResultsDTO.getSuccessfulProfiles();
        List<Long> searchResultsIds = resolveMinimalScrapedSearchResultsIds(successfulProfiles);
        dataLoaderService.saveLinkedinProfiles(successfulProfiles);
        notificationsService.sendAll("LoadBalancer: Profiles has been successfully scraped: ".concat(
                successfulProfiles
                        .stream()
                        .map(linkedinProfile -> searchResultRepository.findById(linkedinProfile.getSearchResult().getId()).get().getInitialDataRecord().getDenovoId())
                        .collect(Collectors.toList())
                        .toString()));
//        processReusableNotScrapedProfiles(scrapingResultsDTO);
//        processNotReusableProfiles(scrapingResultsDTO);
//        List<SearchResult> searchResults = searchResultRepository.findAllByIdIn(searchResultsIds);
//        List<Long> minimalScrapedLinkedinProfiles = searchResults.stream()
//                .map(SearchResult::getLinkedinProfile)
//                .map(LinkedinProfile::getId)
//                .collect(Collectors.toList());
//        triggerChooseCorrectResultEndpoint(minimalScrapedLinkedinProfiles);
    }

//    private void triggerChooseCorrectResultEndpoint(List<Long> minimalScrapedLinkedinProfiles) {
//        log.info("Triggering choose correct result endpoint with linkedin profiles ids: {}", minimalScrapedLinkedinProfiles);
//    }

    private List<Long> resolveMinimalScrapedSearchResultsIds(List<LinkedinProfile> successfulProfiles) {
        List<Long> searchResultIds = successfulProfiles.stream()
                .map(LinkedinProfile::getSearchResult)
                .map(SearchResult::getId)
                .collect(Collectors.toList());
        List<SearchResult> searchResults = searchResultRepository.findAllByIdIn(searchResultIds);
        return searchResults.stream()
                .filter(searchResult -> isNull(searchResult.getLinkedinProfile()))
                .map(SearchResult::getId)
                .collect(Collectors.toList());
    }

    private void processNotReusableProfiles(ScrapingResultsDTO scrapingResultsDTO) {
        List<LinkedinNotReusableProfile> notReusableProfiles = convertResultsToNotReusableProfiles(scrapingResultsDTO);
        dataLoaderService.saveNotReusableProfiles(notReusableProfiles);
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

    private void startScraping(List<InitialDataSearchPosition> initialDataSearchPositions) {
        List<SearchResult> searchResults = initialDataSearchPositions.stream()
                .flatMap(dataSearchPosition ->
                        dataSearchPosition.getInitialData().getSearchResults().stream()
                                .filter(searchResult -> searchResult.getSearchPosition() == dataSearchPosition.getSearchPosition()))
                .collect(Collectors.toList());
        List<List<SearchResult>> resultBatches = ListUtils.partition(searchResults, scrapingProperties.getBatchSize());
        List<List<LinkedinProfileToScrapeDTO>> profileToScrapeBatchesLists = resultBatches.stream()
                .map(searchResult -> searchResult.stream()
                        .map(DTOConverter::searchResultToInitialScrapeDTO)
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
        notificationsService.sendAll("LoadBalancer: Sent "
                .concat(String.valueOf(profileToScrapeBatchesLists.size()))
                .concat(" batches to queue with batch size:")
                .concat(String.valueOf(scrapingProperties.getBatchSize())));
    }
}
