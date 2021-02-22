package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class EducationScraper implements Scraper<String> {

    private static final By EDUCATION_SECTION = By.xpath("//section[@id='education-section']");
    private static final By SHOW_MORE_EDUCATIONS_BUTTON = By.xpath("//section[@id='education-section']//button[contains(text(),'more education')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement educationSection = findElementBy(webDriver, EDUCATION_SECTION);
        if (isNull(educationSection))
            return "";
        scrollToElement(webDriver, educationSection, 200);
        randomSleep(2000, 5000);
        while (nonNull(findElementBy(webDriver, SHOW_MORE_EDUCATIONS_BUTTON))) {
            Actions actions = new Actions(webDriver);
            scrollToAndClickShowMoreEducationsButton(webDriver, actions);
        }
        educationSection = findElementBy(webDriver, EDUCATION_SECTION);
        return getElementHtml(educationSection);
    }

    private void scrollToAndClickShowMoreEducationsButton(WebDriver webDriver, Actions actions) {
        WebElement showMoreEducationsButton = findElementBy(webDriver, SHOW_MORE_EDUCATIONS_BUTTON);
        scrollToElement(webDriver, showMoreEducationsButton, 400);
        clickOnElement(showMoreEducationsButton, actions, randomLong(750, 1500));
        randomSleep(2000, 5000);
    }
}
