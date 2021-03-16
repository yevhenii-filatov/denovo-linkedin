package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.exceptions.linkedin.LinkedinScrapingException;
import com.dataox.linkedinscraper.scraping.scrapers.subscrapers.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LinkedinProfileScraper {

    AboutSectionScraper aboutSectionScraper;
    ActivitiesScraper activitiesScraper;
    EducationScraper educationScraper;
    ExperienceScraper experienceScraper;
    HeaderSectionScraper headerSectionScraper;
    LicenseScraper licenseScraper;
    RecommendationsScraper recommendationsScraper;
    AllSkillsScraper allSkillsScraper;
    SkillsWithEndorsementsScraper skillsWithEndorsementsScraper;
    VolunteersScraper volunteersScraper;
    InterestsScraper interestsScraper;
    AccomplishmentsScraper accomplishmentsScraper;

    public CollectedProfileSourcesDTO scrape(WebDriver webDriver, LinkedinProfileToScrapeDTO profile) {
        try {
            log.info("Scraping profile: {}", profile.getProfileURL());
            webDriver.get(profile.getProfileURL());
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
            profileSourcesDTO.setAccomplishmentsSources(scrapeSafe(webDriver, accomplishmentsScraper, emptyList(), profile.isScrapeAccomplishments()));
            profileSourcesDTO.setInterestsSources(scrapeSafe(webDriver, interestsScraper, emptyList(), profile.isScrapeInterests()));
            profileSourcesDTO.setActivitiesSources(scrapeSafe(webDriver, activitiesScraper, emptyList(), profile.isScrapeActivities()));
            return profileSourcesDTO;
        } catch (Exception e) {
            log.error("Scraper failed to scrape profile: {}", profile.getProfileURL());
            log.error("Error message: {}", e.getMessage());
            throw new LinkedinScrapingException(e);
        }
    }

    private <T> T scrapeSafe(WebDriver webDriver, Scraper<T> scraper, T defaultValue, boolean optional) {
        return optional ? scraper.scrape(webDriver) : defaultValue;
    }
}
