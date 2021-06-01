package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.sources.InterestsSource;
import com.dataox.linkedinscraper.dto.types.InterestsType;
import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
public class InterestsScraper implements Scraper<List<InterestsSource>> {

    private static final By INTERESTS_SECTION = By.xpath("//section[contains(@class,'v-interests-section')]");
    private static final By SEE_ALL_BUTTON = By.xpath("//section[contains(@class,'v-interests-section')]//a/span[text()='See all']");
    private static final By POPUP_INTERESTS_WINDOW = By.xpath("//div[@role='dialog']");
    private static final By TAB_LIST = By.xpath("//ul[@role='tablist']/li");
    private static final By CLOSE_POPUP_BUTTON = By.xpath("//button[@aria-label='Dismiss']");
    private static final By POPUP_SCROLLING_AREA = By.xpath("//div[@data-test-modal]//div[contains(@class,'entity-all pv-interests-list')]");
    private static final int SCROLL_STEP = 500;

    @Override
    public List<InterestsSource> scrape(WebDriver webDriver) {
        WebElement interestsSection = findElementBy(webDriver, INTERESTS_SECTION);
        if (isNull(interestsSection)) {
            log.info("Interests section is not present");
            return emptyList();
        }
        WebElement seeAllButton = findElementBy(webDriver, SEE_ALL_BUTTON);
        if (isNull(seeAllButton)) {
            log.info("Scraping whole interests section");
            return collectWholeInterestsSection(webDriver);
        }
        log.info("Scraping interests section by categories");
        return collectInterestsByGroups(webDriver);
    }

    private List<InterestsSource> collectWholeInterestsSection(WebDriver webDriver) {
        WebElement interestsSection = findElementBy(webDriver, INTERESTS_SECTION);
        scrollToElement(webDriver, interestsSection, 400);
        return singletonList(new InterestsSource(InterestsType.DEFAULT, getElementHtml(interestsSection)));
    }

    private List<InterestsSource> collectInterestsByGroups(WebDriver webDriver) {
        List<InterestsSource> interestsSources = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(webDriver, 120);
        Actions actions = new Actions(webDriver);

        clickSeeAllButton(webDriver, wait, actions);
        List<WebElement> interestsTabs = webDriver.findElements(TAB_LIST);
        if (interestsTabs.isEmpty())
            throw ElementNotFoundException.create("Interests tabs");

        for (WebElement tab : interestsTabs) {
            clickOnElement(tab, actions);
            randomSleep(1500, 2000);
            WebElement scrollingArea = findWebElementBy(webDriver, POPUP_SCROLLING_AREA)
                    .orElseThrow(() -> ElementNotFoundException.create("Interests tab scrolling area"));
            scrollToTheBottomOfElement(webDriver, scrollingArea, SCROLL_STEP);
            WebElement interestsPopup = findWebElementBy(webDriver, POPUP_INTERESTS_WINDOW)
                    .orElseThrow(() -> ElementNotFoundException.create("Interests popup window"));
            String group = tab.getText();
            interestsSources.add(new InterestsSource(group, getElementHtml(interestsPopup)));
            randomSleep(1500, 4000);
        }
        WebElement closePopupButton = findWebElementBy(webDriver, CLOSE_POPUP_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.create("Close interests popup window button"));
        clickOnElement(closePopupButton, actions);
        return interestsSources;
    }


    private void clickSeeAllButton(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        WebElement seeAllButton = findWebElementBy(webDriver, SEE_ALL_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.create("See all interests button"));
        scrollToElement(webDriver, seeAllButton, 450);
        randomSleep(2500, 4500);
        clickOnElement(seeAllButton, actions);
        wait.until(ExpectedConditions.presenceOfElementLocated(POPUP_INTERESTS_WINDOW));
    }
}
