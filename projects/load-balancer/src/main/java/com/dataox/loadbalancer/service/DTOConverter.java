package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.loadbalancer.domain.dto.LinkedinProfileToUpdateDTO;
import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
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

    public static LinkedinProfileToScrapeDTO profileToInitialScrapeDTO(SearchResult searchResult) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setProfileURL(searchResult.getUrl());
        profileToScrapeDTO.setScrapeSkills(true);
        profileToScrapeDTO.setScrapeActivities(true);
        profileToScrapeDTO.setScrapeInterests(true);
        profileToScrapeDTO.setScrapeVolunteer(true);
        profileToScrapeDTO.setScrapeLicenses(true);
        profileToScrapeDTO.setScrapeAccomplishments(true);
        profileToScrapeDTO.setScrapeRecommendations(true);
        profileToScrapeDTO.setSearchResultId(searchResult.getId());
        return profileToScrapeDTO;
    }

    public static LinkedinNotReusableProfile toNotReusableProfile(LinkedinProfileToScrapeDTO profileToScrapeDTO) {
        LinkedinNotReusableProfile notReusableProfile = new LinkedinNotReusableProfile();
        notReusableProfile.setScrapeAccomplishments(profileToScrapeDTO.isScrapeAccomplishments());
        notReusableProfile.setScrapeActivities(profileToScrapeDTO.isScrapeActivities());
        notReusableProfile.setScrapeInterests(profileToScrapeDTO.isScrapeInterests());
        notReusableProfile.setScrapeLicenses(profileToScrapeDTO.isScrapeLicenses());
        notReusableProfile.setScrapeSkills(profileToScrapeDTO.isScrapeSkills());
        notReusableProfile.setScrapeVolunteer(profileToScrapeDTO.isScrapeVolunteer());
        notReusableProfile.setScrapeRecommendations(profileToScrapeDTO.isScrapeRecommendations());
        return notReusableProfile;
    }

    public static LinkedinProfileToScrapeDTO toScrapeDTO(LinkedinNotReusableProfile notReusableProfile) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setScrapeAccomplishments(notReusableProfile.isScrapeAccomplishments());
        profileToScrapeDTO.setScrapeActivities(notReusableProfile.isScrapeActivities());
        profileToScrapeDTO.setScrapeInterests(notReusableProfile.isScrapeInterests());
        profileToScrapeDTO.setScrapeLicenses(notReusableProfile.isScrapeLicenses());
        profileToScrapeDTO.setScrapeSkills(notReusableProfile.isScrapeSkills());
        profileToScrapeDTO.setScrapeVolunteer(notReusableProfile.isScrapeVolunteer());
        profileToScrapeDTO.setScrapeRecommendations(notReusableProfile.isScrapeRecommendations());
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
            LinkedinProfileToScrapeDTO linkedinProfileToScrapeDTO = DTOConverter.toScrapeDTO(profileToUpdateDTO);
            linkedinProfileToScrapeDTO.setProfileURL(searchResult.getUrl());
            linkedinProfileToScrapeDTO.setSearchResultId(searchResult.getId());
            profileToScrapeDTOS.add(linkedinProfileToScrapeDTO);
        }
        return profileToScrapeDTOS;
    }

    public static LinkedinProfileToScrapeDTO toScrapeDTO(LinkedinProfileToUpdateDTO profileToUpdateDTO) {
        LinkedinProfileToScrapeDTO profileToScrapeDTO = new LinkedinProfileToScrapeDTO();
        profileToScrapeDTO.setScrapeAccomplishments(profileToUpdateDTO.isScrapeAccomplishments());
        profileToScrapeDTO.setScrapeActivities(profileToUpdateDTO.isScrapeActivities());
        profileToScrapeDTO.setScrapeInterests(profileToUpdateDTO.isScrapeInterests());
        profileToScrapeDTO.setScrapeLicenses(profileToUpdateDTO.isScrapeLicenses());
        profileToScrapeDTO.setScrapeSkills(profileToUpdateDTO.isScrapeSkills());
        profileToScrapeDTO.setScrapeVolunteer(profileToUpdateDTO.isScrapeVolunteer());
        profileToScrapeDTO.setScrapeRecommendations(profileToUpdateDTO.isScrapeRecommendations());
        return profileToScrapeDTO;
    }
}
