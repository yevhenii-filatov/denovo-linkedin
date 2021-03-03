package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.scraping.exceptions.LinkedinException;
import com.dataox.linkedinscraper.scraping.scrapers.subscrapers.*;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class LinkedinProfileScraper {

    private final LinkedinErrorDetector errorDetector;
    private final AboutSectionScraper aboutSectionScraper;
    private final ActivitiesScraper activitiesScraper;
    private final EducationScraper educationScraper;
    private final ExperienceScraper experienceScraper;
    private final HeaderSectionScraper headerSectionScraper;
    private final LicenseScraper licenseScraper;
    private final RecommendationsScraper recommendationsScraper;
    private final AllSkillsScraper allSkillsScraper;
    private final SkillsWithEndorsementsScraper skillsWithEndorsementsScraper;
    private final VolunteersScraper volunteersScraper;
    private final InterestsScraper interestsScraper;
    private final AccomplishmentsScraper accomplishmentsScraper;

    public CollectedProfileSourcesDTO scrape(WebDriver webDriver, LinkedinProfileToScrapeDTO profile) {
        log.info("Scraping profile: {}", profile.getProfileURL());
        webDriver.get(profile.getProfileURL());
        LinkedinError linkedinError = errorDetector.detect(webDriver);
        if (!linkedinError.equals(LinkedinError.NO_ERRORS))
            throw new LinkedinException(linkedinError.getMessage());
        CollectedProfileSourcesDTO profileSourcesDTO = new CollectedProfileSourcesDTO();
        profileSourcesDTO.setProfileUrl(profile.getProfileURL());
        profileSourcesDTO.setHeaderSectionSource(headerSectionScraper.scrape(webDriver));
        profileSourcesDTO.setProfilePhotoUrl(headerSectionScraper.scrapeProfilePhotoUrl(webDriver));
        profileSourcesDTO.setAboutSectionSource(aboutSectionScraper.scrape(webDriver));
        profileSourcesDTO.setExperiencesSource(experienceScraper.scrape(webDriver));
        profileSourcesDTO.setEducationsSource(educationScraper.scrape(webDriver));
        profileSourcesDTO.setLicenseSource(scrapeSafe(webDriver, licenseScraper, EMPTY, profile.isScrapeLicenses()));
        profileSourcesDTO.setVolunteersSource(scrapeSafe(webDriver, volunteersScraper, EMPTY, profile.isScrapeVolunteer()));
        profileSourcesDTO.setSkillsWithEndorsementsSources(scrapeSafe(webDriver, skillsWithEndorsementsScraper, emptyList(), profile.isScrapeSkills()));
        profileSourcesDTO.setAllSkillsSource(scrapeSafe(webDriver, allSkillsScraper, EMPTY, profile.isScrapeSkills()));
        profileSourcesDTO.setRecommendationsSources(scrapeSafe(webDriver, recommendationsScraper, emptyList(), profile.isScrapeRecommendations()));
        profileSourcesDTO.setAccomplishmentsSources(accomplishmentsScraper.scrape(webDriver));
        profileSourcesDTO.setInterestsSources(scrapeSafe(webDriver, interestsScraper, emptyList(), profile.isScrapeInterests()));
        profileSourcesDTO.setActivitiesSources(scrapeSafe(webDriver, activitiesScraper, emptyList(), profile.isScrapeActivities()));
        return profileSourcesDTO;
    }

    private <T> T scrapeSafe(WebDriver webDriver, Scraper<T> scraper, T defaultValue, boolean optional) {
        return optional ? scraper.scrape(webDriver) : defaultValue;
    }
}
