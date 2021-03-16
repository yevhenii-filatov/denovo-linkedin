package com.dataox.linkedinscraper.scraping.scrapers;

import org.openqa.selenium.WebDriver;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
public interface Scraper<T> {
    T scrape(WebDriver webDriver);
}
