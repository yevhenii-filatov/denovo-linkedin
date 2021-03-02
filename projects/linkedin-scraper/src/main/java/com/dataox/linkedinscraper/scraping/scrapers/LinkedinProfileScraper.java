package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.scraping.exceptions.LinkedinException;
import com.dataox.linkedinscraper.scraping.scrapers.subscrapers.*;
import com.dataox.linkedinscraper.service.error.detector.LinkedinError;
import com.dataox.linkedinscraper.service.error.detector.LinkedinErrorDetector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
        profileSourcesDTO.setLicenseSource(licenseScraper.scrape(webDriver));
        profileSourcesDTO.setVolunteersSource(volunteersScraper.scrape(webDriver));
        profileSourcesDTO.setSkillsWithEndorsementsSources(skillsWithEndorsementsScraper.scrape(webDriver));
        profileSourcesDTO.setAllSkillsSource(allSkillsScraper.scrape(webDriver));
        profileSourcesDTO.setRecommendationsSources(recommendationsScraper.scrape(webDriver));
        profileSourcesDTO.setAccomplishmentsSources(accomplishmentsScraper.scrape(webDriver));
        profileSourcesDTO.setInterestsSources(interestsScraper.scrape(webDriver));
        profileSourcesDTO.setActivitiesSources(activitiesScraper.scrape(webDriver));
        return profileSourcesDTO;
    }
}
