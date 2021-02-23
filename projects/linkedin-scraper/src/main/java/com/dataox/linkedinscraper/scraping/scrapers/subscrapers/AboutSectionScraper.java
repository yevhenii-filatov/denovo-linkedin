package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class AboutSectionScraper implements Scraper<String> {

    private static final By ABOUT_SELECTOR = By.xpath("//section[contains(@class,'pv-about-section')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-about-section')]//a[@data-test-line-clamp-show-more-button][text()='see more']");

    private void clickSeeMoreButton(WebDriver webDriver, WebElement seeMoreButton) {
        Actions actions = new Actions(webDriver);
        clickOnElement(seeMoreButton, actions, randomLong(750, 1500));
    }

    @Override
    public String scrape(WebDriver webDriver) {
        randomSleep(2000, 5000);
        WebElement aboutSection = findWebElementBy(webDriver, ABOUT_SELECTOR)
                .orElseThrow(() -> ElementNotFoundException.notFound("About section"));
        scrollToElement(webDriver, aboutSection, 200);
        findWebElementBy(webDriver, SEE_MORE_BUTTON).ifPresent(webElement -> clickSeeMoreButton(webDriver, webElement));
        aboutSection = findElementBy(webDriver, ABOUT_SELECTOR);
        return getElementHtml(aboutSection);
    }
}
