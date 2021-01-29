package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class LinkedinProfileScraper extends AbstractLinkedinProfileScraper {

    public LinkedinProfileScraper(LoginService loginService, WebDriver webDriver) {
        super(loginService, webDriver);
    }

    @Override
    public String scrapeHeaderSection(WebDriver webDriver) {
        return new HeaderSectionScraper().scrape(webDriver);
    }

    @Override
    public String scrapeAboutSection(WebDriver webDriver) {
        return new AboutSectionScraper().scrape(webDriver);
    }

    @Override
    public String scrapeExperienceSection(WebDriver webDriver) {
        return new ExperienceScraper().scrape(webDriver);
    }

    @Override
    public String scrapeEducationSection(WebDriver webDriver) {
        return new EducationScraper().scrape(webDriver);
    }

    @Override
    public String scrapeRecommendationsSection(WebDriver webDriver) {
        return new RecommendationsScraper().scrape(webDriver);
    }

    @Override
    public String scrapeSkillsSection(WebDriver webDriver) {
        return new SkillsScraper().scrape(webDriver);
    }

    @Override
    public String scrapeLicenseSection(WebDriver webDriver) {
        return new LicenseScraper().scrape(webDriver);
    }

    @Override
    public String scrapeVolunteersSection(WebDriver webDriver) {
        return new VolunteersScraper().scrape(webDriver);
    }

    @Override
    public Map<String, String> scrapeInterestsSources(WebDriver webDriver) {
        return new InterestsScraper().scrape(webDriver);
    }

    @Override
    public Map<String, String> scrapeActivitiesSection(WebDriver webDriver) {
        return new ActivitiesScraper().scrape(webDriver);
    }
}
