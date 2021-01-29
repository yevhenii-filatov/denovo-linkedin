package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
public class AboutSectionScraper implements Scraper<String> {

    private static final By ABOUT_SELECTOR = By.xpath("//section[contains(@class,'pv-about-section')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-about-section')]//a[@data-test-line-clamp-show-more-button][text()='see more']");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement seeMoreButton = WebDriverUtils.findElementBy(webDriver, SEE_MORE_BUTTON);
        if (nonNull(seeMoreButton))
            new Actions(webDriver).moveToElement(seeMoreButton).pause(1500).click().perform();
        WebElement aboutSection = WebDriverUtils.findElementBy(webDriver, ABOUT_SELECTOR);
        return aboutSection.getAttribute("outerHTML");
    }
}
