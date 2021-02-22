package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 09/02/2021
 */
@Service
public class AccomplishmentsScraper implements Scraper<List<String>> {

    private static final By ACCOMPLISHMENTS_SECTION = By.className("pv-accomplishments-section");
    private static final By ACCOMPLISHMENTS_SUBSECTIONS = By.xpath("//section[contains(@class,'pv-accomplishments-section')]/div");
    private static final By EXPAND_SUBSECTION_BUTTON = By.tagName("button");
    private static final By SHOW_MORE_BUTTON = By.className("pv-profile-section__see-more-inline");

    @Override
    public List<String> scrape(WebDriver webDriver) {
        Actions actions = new Actions(webDriver);
        WebElement accSection = findElementBy(webDriver, ACCOMPLISHMENTS_SECTION);
        if (isNull(accSection)) {
            return Collections.emptyList();
        }
        List<String> sources = new ArrayList<>();
        scrollToElement(webDriver, accSection, 400);
        List<WebElement> subSections = webDriver.findElements(ACCOMPLISHMENTS_SUBSECTIONS);
        WebElement subSection;
        for (int i = 0; i < subSections.size(); i++) {
            subSection = subSections.get(i);
            WebElement expandButton = subSection.findElement(EXPAND_SUBSECTION_BUTTON);
            clickOnElement(expandButton, actions, randomLong(750, 1500));
            randomSleep(2500, 3500);
            subSection = reFindSubSection(webDriver, i);
            WebElement showMoreButton = findElement(subSection, SHOW_MORE_BUTTON);
            while (nonNull(showMoreButton)) {
                scrollToAndClickOnElement(webDriver, actions, showMoreButton);
                randomSleep(2500, 3500);
                subSection = reFindSubSection(webDriver, i);
                showMoreButton = findElement(subSection, SHOW_MORE_BUTTON);
            }

            sources.add(getElementHtml(subSection));
        }
        return sources;
    }

    private WebElement findElement(WebElement parent, By by) {
        WebElement element;
        try {
            element = parent.findElement(by);
        } catch (NoSuchElementException e) {
            return null;
        }
        return element;
    }

    private WebElement reFindSubSection(WebDriver webDriver, int index) {
        return webDriver.findElements(ACCOMPLISHMENTS_SUBSECTIONS).get(index);
    }
}
