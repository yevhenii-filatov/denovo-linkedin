package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.WebDriverUtils;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Getter
@Setter
public class HeaderSectionScraper implements Scraper<String> {

    private final By HEADER_SELECTOR = By.xpath("//section[contains(@class,'pv-top-card')]");

    @Override
    public String scrape(WebDriver webDriver) {
        return WebDriverUtils.findElementBy(webDriver, HEADER_SELECTOR).getAttribute("outerHTML");
    }
}
