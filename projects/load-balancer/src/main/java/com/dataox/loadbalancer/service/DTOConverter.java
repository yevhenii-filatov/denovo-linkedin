package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import com.dataox.loadbalancer.dto.InitialDataDTO;
import com.dataox.loadbalancer.dto.LinkedinProfileToUpdateDTO;
import com.dataox.loadbalancer.exception.DTONotFoundException;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@UtilityClass
public class DTOConverter {

    public static LinkedinProfileToScrapeDTO searchResultToInitialScrapeDTO(SearchResult searchResult) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setProfileURL(searchResult.getUrl());
        OptionalFieldsContainer optionalFieldsContainer = new OptionalFieldsContainer();
        optionalFieldsContainer.setScrapeSkills(true);
        optionalFieldsContainer.setScrapeActivities(true);
        optionalFieldsContainer.setScrapeInterests(true);
        optionalFieldsContainer.setScrapeVolunteer(true);
        optionalFieldsContainer.setScrapeLicenses(true);
        optionalFieldsContainer.setScrapeAccomplishments(true);
        optionalFieldsContainer.setScrapeRecommendations(true);
        profileToScrapeDTO.setSearchResultId(searchResult.getId());
        profileToScrapeDTO.setOptionalFieldsContainer(optionalFieldsContainer);
        profileToScrapeDTO.setDenovoId(searchResult.getInitialDataRecord().getDenovoId());
        return profileToScrapeDTO;
    }

    public static LinkedinNotReusableProfile toNotReusableProfile(LinkedinProfileToScrapeDTO profileToScrapeDTO) {
        LinkedinNotReusableProfile notReusableProfile = new LinkedinNotReusableProfile();
        notReusableProfile.setOptionalFieldsContainer(profileToScrapeDTO.getOptionalFieldsContainer());
        return notReusableProfile;
    }

    public static LinkedinProfileToScrapeDTO toScrapeDTO(LinkedinNotReusableProfile notReusableProfile) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setOptionalFieldsContainer(notReusableProfile.getOptionalFieldsContainer());
        profileToScrapeDTO.setProfileURL(notReusableProfile.getSearchResult().getUrl());
        profileToScrapeDTO.setSearchResultId(notReusableProfile.getSearchResult().getId());
        return profileToScrapeDTO;
    }

    public static List<LinkedinProfileToScrapeDTO> convertToProfileToScrapeDTOS(List<LinkedinProfileToUpdateDTO> profileToUpdateDTOS,
                                                                                List<LinkedinProfile> toUpdateProfiles) {
        List<LinkedinProfileToScrapeDTO> profileToScrapeDTOS = new ArrayList<>();
        LinkedinProfileToUpdateDTO profileToUpdateDTO;
        for (LinkedinProfile toUpdateProfile : toUpdateProfiles) {
            Long profileId = toUpdateProfile.getId();
            SearchResult searchResult = toUpdateProfile.getSearchResult();
            profileToUpdateDTO = profileToUpdateDTOS.stream()
                    .filter(linkedinProfileToUpdateDTO -> linkedinProfileToUpdateDTO.getLinkedinProfileId().equals(profileId))
                    .findFirst()
                    .orElseThrow(() -> new DTONotFoundException("profileToUpdateDTO with required profile id was not found"));
            LinkedinProfileToScrapeDTO linkedinProfileToScrapeDTO = toScrapeDTO(profileToUpdateDTO);
            linkedinProfileToScrapeDTO.setProfileURL(searchResult.getUrl());
            linkedinProfileToScrapeDTO.setSearchResultId(searchResult.getId());
            profileToScrapeDTOS.add(linkedinProfileToScrapeDTO);
        }
        return profileToScrapeDTOS;
    }

    public static LinkedinProfileToScrapeDTO toScrapeDTO(LinkedinProfileToUpdateDTO profileToUpdateDTO) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setOptionalFieldsContainer(profileToUpdateDTO.getOptionalFieldsContainer());
        return profileToScrapeDTO;
    }

    public static List<InitialData> toInitialData(List<InitialDataDTO> initialDataDTOS) {
        List<InitialData> initialDataList = new ArrayList<>();
        for (InitialDataDTO initialDataDTO : initialDataDTOS) {
            InitialData initialData = new InitialData();
            initialData.setDenovoId(initialDataDTO.getDenovoId());
            initialData.setFirstName(initialDataDTO.getFirstName());
            initialData.setLastName(initialDataDTO.getLastName());
            initialData.setFirmName(initialDataDTO.getFirmName());
            initialData.setSearched(false);
            initialDataList.add(initialData);
        }
        return initialDataList;
    }

    public static List<LinkedinProfileToScrapeDTO> toMinimalScrapeDTOS(List<SearchResult> searchResults) {
        List<LinkedinProfileToScrapeDTO> profilesToScrape = new ArrayList<>();
        for (SearchResult searchResult : searchResults) {
            LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
            profileToScrapeDTO.setProfileURL(searchResult.getUrl());
            profileToScrapeDTO.setSearchResultId(searchResult.getId());
            profileToScrapeDTO.setOptionalFieldsContainer(new OptionalFieldsContainer());
            profilesToScrape.add(profileToScrapeDTO);
        }
        return profilesToScrape;
    }
}
