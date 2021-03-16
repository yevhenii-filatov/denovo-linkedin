package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
public class VolunteersScraper implements Scraper<String> {

    private static final By VOLUNTEERS_SECTION = By.xpath("//section[contains(@class,'pv-profile-section volunteering-section')]");
    private static final By SHOW_MORE_EXPERIENCE = By.xpath("//section[contains(@class,'pv-profile-section volunteering-section')]//button[contains(text(),'more experience')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement volunteersSection = findElementBy(webDriver, VOLUNTEERS_SECTION);
        if (isNull(volunteersSection)) {
            log.info("Volunteers section is not present");
            return "";
        }
        log.info("Scraping volunteers section");
        scrollToElement(webDriver, volunteersSection, 300);
        randomSleep(2000, 5000);
        openAllVolunteersExperience(webDriver);
        randomSleep(1250, 3000);
        volunteersSection = findWebElementBy(webDriver, VOLUNTEERS_SECTION)
                .orElseThrow(() -> ElementNotFoundException.create("Volunteers section"));
        return getElementHtml(volunteersSection);
    }

    private void openAllVolunteersExperience(WebDriver webDriver) {
        WebElement showMoreExperienceButton = findElementBy(webDriver, SHOW_MORE_EXPERIENCE);
        while (nonNull(showMoreExperienceButton)) {
            Actions actions = new Actions(webDriver);
            scrollToAndClickOnElement(webDriver, actions, showMoreExperienceButton);
            randomSleep(3500, 5500);
            showMoreExperienceButton = findElementBy(webDriver, SHOW_MORE_EXPERIENCE);
        }
    }

}
