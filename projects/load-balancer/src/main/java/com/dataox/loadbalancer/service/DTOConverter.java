package com.dataox.loadbalancer.service;

import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
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
}
