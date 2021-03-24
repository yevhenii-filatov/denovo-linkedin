package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import lombok.experimental.UtilityClass;

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
}
