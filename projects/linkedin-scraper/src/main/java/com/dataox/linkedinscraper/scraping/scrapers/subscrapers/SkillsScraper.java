package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class SkillsScraper implements Scraper<String> {

    private static final By SKILLS_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]");

    @Override
    public String scrape(WebDriver webDriver) {
        return null;
    }
}
