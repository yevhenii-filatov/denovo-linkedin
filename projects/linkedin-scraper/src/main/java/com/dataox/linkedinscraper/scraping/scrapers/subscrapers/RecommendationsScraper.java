package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.types.RecommendationType;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class RecommendationsScraper implements Scraper<Map<String, String>> {

    private static final By RECOMMENDATION_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]");
    private static final By ZERO_RECEIVED_RECOMMENDATIONS = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//button[text()='Received (0)']");
    private static final By ZERO_GIVEN_RECOMMENDATIONS = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//button[text()='Given (0)']");
    private static final By GIVEN_RECOMMENDATIONS_TAB = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//button[2]");
    private static final By RECEIVED_RECOMMENDATIONS_TAB = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//button[1]");
    private static final By SHOW_MORE_RECOMMENDATIONS_BUTTON = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//button[contains(text(),'Show more')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]//a[text()='See more'][@data-test-line-clamp-show-more-button]");

    @Override
    public Map<String, String> scrape(WebDriver webDriver) {
        WebElement recommendationSection = findElementBy(webDriver, RECOMMENDATION_SECTION);
        if (isNull(recommendationSection))
            return Collections.emptyMap();
        else
            scrollToElement(webDriver, recommendationSection, 450);

        Map<String, String> recommendations = new HashMap<>();
        if (isNull(findElementBy(webDriver, ZERO_RECEIVED_RECOMMENDATIONS)))
            recommendations.put(RecommendationType.RECEIVED, scrapeTab(webDriver, RECEIVED_RECOMMENDATIONS_TAB));
        if (isNull(findElementBy(webDriver, ZERO_GIVEN_RECOMMENDATIONS)))
            recommendations.put(RecommendationType.GIVEN, scrapeTab(webDriver, GIVEN_RECOMMENDATIONS_TAB));
        return recommendations;
    }

    private String scrapeTab(WebDriver webDriver, By receivedRecommendationsTab) {
        Actions actions = new Actions(webDriver);
        openRecommendationsTab(webDriver, actions, receivedRecommendationsTab);
        while (nonNull(findElementBy(webDriver, SHOW_MORE_RECOMMENDATIONS_BUTTON))) {
            WebElement showMoreButton = findElementBy(webDriver, SHOW_MORE_RECOMMENDATIONS_BUTTON);
            scrollToElement(webDriver, showMoreButton, 500);
            actions.moveToElement(showMoreButton).pause(randomLong(800, 1500)).click().perform();
            randomSleep(2000, 4000);
        }
        clickAllSeeMoreButton(webDriver);
        return getElementHtml(findElementBy(webDriver, RECOMMENDATION_SECTION));
    }

    private void openRecommendationsTab(WebDriver webDriver, Actions actions, By recommendationsTab) {
        WebElement givenRecommendationsTab = findElementBy(webDriver, recommendationsTab);
        scrollToElement(webDriver, givenRecommendationsTab, 450);
        actions.moveToElement(givenRecommendationsTab).pause(randomLong(1500, 2500)).click().perform();
        randomSleep(2500, 4500);
    }

    private void clickAllSeeMoreButton(WebDriver webDriver) {
        List<WebElement> allSeeMoreButtons = webDriver.findElements(SEE_MORE_BUTTON);
        if (allSeeMoreButtons.isEmpty())
            return;
        WebElement firstSeeMoreButton = allSeeMoreButtons.get(0);
        scrollToElement(webDriver, firstSeeMoreButton, 450);
        for (WebElement seeMoreButton : allSeeMoreButtons) {
            new Actions(webDriver).moveToElement(seeMoreButton).pause(randomLong(1500, 2500)).click().perform();
            randomSleep(1500, 2500);
        }
    }
}
