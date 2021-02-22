package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.*;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class VolunteersScraper implements Scraper<String> {

    private static final By VOLUNTEERS_SECTION = By.xpath("//section[contains(@class,'pv-profile-section volunteering-section')]");
    private static final By SHOW_MORE_EXPERIENCE = By.xpath("//section[contains(@class,'pv-profile-section volunteering-section')]//button[contains(text(),'more experience')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement volunteersSection = findElementBy(webDriver, VOLUNTEERS_SECTION);
        if (isNull(volunteersSection))
            return "";
        scrollToElement(webDriver, volunteersSection, 300);
        randomSleep(2000, 5000);
        while (nonNull(findElementBy(webDriver, SHOW_MORE_EXPERIENCE))) {
            Actions actions = new Actions(webDriver);
            scrollToShowMoreButtonAndClick(webDriver, actions);
            randomSleep(3500, 5500);
        }
        randomSleep(1250, 3000);
        volunteersSection = findElementBy(webDriver, VOLUNTEERS_SECTION);
        return getElementHtml(volunteersSection);
    }

    private void scrollToShowMoreButtonAndClick(WebDriver webDriver, Actions actions) {
        WebElement showMoreExperienceButton = findElementBy(webDriver, SHOW_MORE_EXPERIENCE);
        scrollToElement(webDriver, showMoreExperienceButton, 300);
        clickOnElement(showMoreExperienceButton, actions, randomLong(750, 1500));
    }
}
