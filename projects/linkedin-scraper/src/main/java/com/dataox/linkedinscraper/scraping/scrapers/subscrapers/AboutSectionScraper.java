package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
public class AboutSectionScraper implements Scraper<String> {

    private static final By ABOUT_SELECTOR = By.xpath("//section[contains(@class,'pv-about-section')][not(@style)]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-about-section')]//a[@data-test-line-clamp-show-more-button][text()='see more']");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement aboutSection = findElementBy(webDriver, ABOUT_SELECTOR);
        if (isNull(aboutSection)) {
            log.info("About section is not present");
            return "";
        }
        log.info("Scraping about section");
        randomSleep(2000, 5000);
        scrollToElement(webDriver, aboutSection, 200);
        findWebElementBy(webDriver, SEE_MORE_BUTTON)
                .ifPresent(webElement -> clickSeeMoreButton(webDriver, webElement));
        aboutSection = findElementBy(webDriver, ABOUT_SELECTOR);
        return getElementHtml(aboutSection);
    }

    private void clickSeeMoreButton(WebDriver webDriver, WebElement seeMoreButton) {
        Actions actions = new Actions(webDriver);
        clickOnElement(seeMoreButton, actions, randomLong(750, 1500));
    }
}
