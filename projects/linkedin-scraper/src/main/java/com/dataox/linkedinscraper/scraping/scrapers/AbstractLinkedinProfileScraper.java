package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@RequiredArgsConstructor
public abstract class AbstractLinkedinProfileScraper {

    private static final String LINKEDIN_LOGIN_PAGE_URL = "https://www.linkedin.com/";
    private final LoginService loginService;
    private final WebDriver webDriver;

    public CollectedProfileSourcesDTO scrape(String profileUrl) {
        webDriver.get(LINKEDIN_LOGIN_PAGE_URL);
        loginService.performLogin(webDriver);
        webDriver.get(profileUrl);
        CollectedProfileSourcesDTO profileSourcesDTO = new CollectedProfileSourcesDTO();
        profileSourcesDTO.setHeaderSectionSource(scrapeHeaderSection(webDriver));
        profileSourcesDTO.setAboutSectionSource(scrapeAboutSection(webDriver));
        profileSourcesDTO.setExperiencesSource(scrapeExperienceSection(webDriver));
        profileSourcesDTO.setEducationsSource(scrapeEducationSection(webDriver));
        profileSourcesDTO.setRecommendationsSource(scrapeRecommendationsSection(webDriver));
        profileSourcesDTO.setSkillsSource(scrapeSkillsSection(webDriver));
        profileSourcesDTO.setLicenseSource(scrapeLicenseSection(webDriver));
        profileSourcesDTO.setVolunteersSource(scrapeVolunteersSection(webDriver));
        profileSourcesDTO.setInterestsSources(scrapeInterestsSources(webDriver));
        profileSourcesDTO.setUrlAndActivitiesSources(scrapeActivitiesSection(webDriver));
        return profileSourcesDTO;
    }

    public abstract String scrapeHeaderSection(WebDriver webDriver);

    public abstract String scrapeAboutSection(WebDriver webDriver);

    public abstract String scrapeExperienceSection(WebDriver webDriver);

    public abstract String scrapeEducationSection(WebDriver webDriver);

    public abstract String scrapeRecommendationsSection(WebDriver webDriver);

    public abstract String scrapeSkillsSection(WebDriver webDriver);

    public abstract String scrapeLicenseSection(WebDriver webDriver);

    public abstract String scrapeVolunteersSection(WebDriver webDriver);

    public abstract Map<String, String> scrapeInterestsSources(WebDriver webDriver);

    public abstract Map<String, String> scrapeActivitiesSection(WebDriver webDriver);
}
