package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.dataox.WebDriverUtils.findElementBy;
import static com.dataox.WebDriverUtils.getElementHtml;

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
        return getElementHtml(findElementBy(webDriver, HEADER_SELECTOR));
    }
}
