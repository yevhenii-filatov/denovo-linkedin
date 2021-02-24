package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

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
public class EducationScraper implements Scraper<String> {

    private static final By EDUCATION_SECTION = By.xpath("//section[@id='education-section']");
    private static final By SHOW_MORE_EDUCATIONS_BUTTON = By.xpath("//section[@id='education-section']//button[contains(text(),'more education')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement educationSection = findElementBy(webDriver, EDUCATION_SECTION);
        if (isNull(educationSection)) {
            log.info("Education section is not present");
            return "";
        }
        log.info("Scraping education section");
        Actions actions = new Actions(webDriver);
        scrollToElement(webDriver, educationSection, 200);
        randomSleep(2000, 5000);
        WebElement showMoreEducationsButton = findElementBy(webDriver, SHOW_MORE_EDUCATIONS_BUTTON);
        while (nonNull(showMoreEducationsButton)) {
            showMoreEducationsButton = findElementBy(webDriver, SHOW_MORE_EDUCATIONS_BUTTON);
            scrollToAndClickOnElement(webDriver, actions, showMoreEducationsButton);
            randomSleep(2500, 3500);
        }
        educationSection = findElementBy(webDriver, EDUCATION_SECTION);
        return getElementHtml(educationSection);
    }

}
