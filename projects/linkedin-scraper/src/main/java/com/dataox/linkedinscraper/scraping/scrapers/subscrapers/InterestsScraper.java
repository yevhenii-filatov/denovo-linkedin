package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class InterestsScraper implements Scraper<Map<String, String>> {

    @Override
    public Map<String, String> scrape(WebDriver webDriver) {
        return null;
    }
}
