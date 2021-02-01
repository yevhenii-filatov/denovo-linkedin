package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.scraping.scrapers.subscrapers.*;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Primary
@Service
@RequiredArgsConstructor
public class LinkedinProfileScraper {

    private static final String LINKEDIN_LOGIN_PAGE_URL = "https://www.linkedin.com/";
    private final LoginService loginService;
    private final AboutSectionScraper aboutSectionScraper;
    private final ActivitiesScraper activitiesScraper;
    private final EducationScraper educationScraper;
    private final ExperienceScraper experienceScraper;
    private final HeaderSectionScraper headerSectionScraper;
    private final InterestsScraper interestsScraper;
    private final LicenseScraper licenseScraper;
    private final RecommendationsScraper recommendationsScraper;
    private final SkillsScraper skillsScraper;
    private final VolunteersScraper volunteersScraper;

    public CollectedProfileSourcesDTO scrape(WebDriver webDriver, String profileUrl) {
        webDriver.get(LINKEDIN_LOGIN_PAGE_URL);
        loginService.performLogin(webDriver);
        webDriver.get(profileUrl);
        CollectedProfileSourcesDTO profileSourcesDTO = new CollectedProfileSourcesDTO();
        profileSourcesDTO.setHeaderSectionSource(headerSectionScraper.scrape(webDriver));
        profileSourcesDTO.setAboutSectionSource(aboutSectionScraper.scrape(webDriver));
        profileSourcesDTO.setExperiencesSource(experienceScraper.scrape(webDriver));
        profileSourcesDTO.setEducationsSource(educationScraper.scrape(webDriver));
        profileSourcesDTO.setLicenseSource(licenseScraper.scrape(webDriver));
        profileSourcesDTO.setVolunteersSource(volunteersScraper.scrape(webDriver));
        profileSourcesDTO.setSkillsSource(skillsScraper.scrape(webDriver));
        profileSourcesDTO.setRecommendationsSource(recommendationsScraper.scrape(webDriver));
        profileSourcesDTO.setInterestsSources(interestsScraper.scrape(webDriver));
        profileSourcesDTO.setUrlAndActivitiesSources(activitiesScraper.scrape(webDriver));
        return profileSourcesDTO;
    }
}
