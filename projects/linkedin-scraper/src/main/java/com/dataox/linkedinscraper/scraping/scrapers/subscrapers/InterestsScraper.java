package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.types.InterestsType;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class InterestsScraper implements Scraper<Map<String, String>> {

    private static final By INTERESTS_SECTION = By.xpath("//section[contains(@class,'v-interests-section')]");
    private static final By SEE_ALL_BUTTON = By.xpath("//section[contains(@class,'v-interests-section')]//a/span[text()='See all']");
    private static final By POPUP_INTERESTS_WINDOW = By.xpath("//div[@role='dialog']");
    private static final By TAB_LIST = By.xpath("//ul[@role='tablist']/li");
    private static final By CLOSE_POPUP_BUTTON = By.xpath("//button[@aria-label='Dismiss']");

    @Override
    public Map<String, String> scrape(WebDriver webDriver) {
        if (isNull(findElementBy(webDriver, INTERESTS_SECTION)))
            return Collections.emptyMap();
        if (isNull(findElementBy(webDriver, SEE_ALL_BUTTON)))
            return collectWhileInterestsSection(webDriver);
        return collectInterestsByGroups(webDriver);
    }

    private Map<String, String> collectWhileInterestsSection(WebDriver webDriver) {
        WebElement interestsSection = findElementBy(webDriver, INTERESTS_SECTION);
        scrollToElement(webDriver, interestsSection, 400);
        return Collections.singletonMap(InterestsType.DEFAULT, getElementHtml(interestsSection));
    }

    private Map<String, String> collectInterestsByGroups(WebDriver webDriver) {
        Map<String, String> groupAndInterestSource = new HashMap<>();
        WebDriverWait wait = new WebDriverWait(webDriver, 45);
        Actions actions = new Actions(webDriver);

        clickSeeAllButton(webDriver, wait, actions);
        for (WebElement tab : webDriver.findElements(TAB_LIST)) {
            clickOnElement(tab,actions,randomLong(750,1500));
            randomSleep(1500, 4000);
            WebElement interestsPopup = findElementBy(webDriver, POPUP_INTERESTS_WINDOW);
            String group = tab.getText();
            groupAndInterestSource.put(group, getElementHtml(interestsPopup));
            randomSleep(1500, 4000);
        }
        clickOnElement(findElementBy(webDriver, CLOSE_POPUP_BUTTON),actions,randomLong(750,1500));
        return groupAndInterestSource;
    }

    private void clickSeeAllButton(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        WebElement seeAllButton = findElementBy(webDriver, SEE_ALL_BUTTON);
        scrollToElement(webDriver, seeAllButton, 450);
        randomSleep(2500, 4500);
        clickOnElement(seeAllButton,actions,randomLong(750,1500));
        wait.until(ExpectedConditions.presenceOfElementLocated(POPUP_INTERESTS_WINDOW));
    }
}
