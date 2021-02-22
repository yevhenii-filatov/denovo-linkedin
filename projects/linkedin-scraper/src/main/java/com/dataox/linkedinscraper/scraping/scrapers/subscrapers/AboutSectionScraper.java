package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.CommonUtils;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.*;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class AboutSectionScraper implements Scraper<String> {

    private static final By ABOUT_SELECTOR = By.xpath("//section[contains(@class,'pv-about-section')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-about-section')]//a[@data-test-line-clamp-show-more-button][text()='see more']");

    @Override
    public String scrape(WebDriver webDriver) {
        randomSleep(2000, 5000);
        WebElement aboutSection = findElementBy(webDriver, ABOUT_SELECTOR);
        scrollToElement(webDriver, aboutSection, 200);
        WebElement seeMoreButton = findElementBy(webDriver, SEE_MORE_BUTTON);
        if (nonNull(seeMoreButton)) {
            Actions actions = new Actions(webDriver);
            clickOnElement(seeMoreButton, actions, randomLong(750, 1500));
        }
        aboutSection = findElementBy(webDriver, ABOUT_SELECTOR);
        return getElementHtml(aboutSection);
    }
}
