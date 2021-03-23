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
        profileToScrapeDTO.setScrapeActivities(false);
        profileToScrapeDTO.setScrapeInterests(false);
        profileToScrapeDTO.setScrapeVolunteer(true);
        profileToScrapeDTO.setScrapeLicenses(true);
        profileToScrapeDTO.setScrapeAccomplishments(false);
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
}
