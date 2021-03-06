package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.sources.RecommendationsSource;
import com.dataox.linkedinscraper.dto.types.RecommendationType;
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
import java.util.Collections;
import java.util.List;

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
public class RecommendationsScraper implements Scraper<List<RecommendationsSource>> {

    private static final By RECOMMENDATION_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')][not(@style)]");
    private static final By ZERO_RECEIVED_RECOMMENDATIONS = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//button[text()='Received (0)']");
    private static final By ZERO_GIVEN_RECOMMENDATIONS = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//button[text()='Given (0)']");
    private static final By GIVEN_RECOMMENDATIONS_TAB = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//button[2]");
    private static final By RECEIVED_RECOMMENDATIONS_TAB = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//button[1]");
    private static final By SHOW_MORE_RECOMMENDATIONS_BUTTON = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//div[@role='tabpanel'][not(@hidden)]//button[contains(text(),'Show ') and contains(text(),'more')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]" +
            "//a[text()='See more'][@data-test-line-clamp-show-more-button]");
    private static final By LOADING_ANIMATION = By.className("artdeco-loader--small");

    @Override
    public List<RecommendationsSource> scrape(WebDriver webDriver) {
        WebElement recommendationSection = findElementBy(webDriver, RECOMMENDATION_SECTION);
        if (isNull(recommendationSection)) {
            log.info("Recommendations section is not present");
            return Collections.emptyList();
        }
        scrollToElement(webDriver, recommendationSection, 450);

        log.info("Scraping recommendations section");
        List<RecommendationsSource> recommendationsSources = new ArrayList<>();
        if (isNull(findElementBy(webDriver, ZERO_RECEIVED_RECOMMENDATIONS)))
            recommendationsSources.add(new RecommendationsSource(RecommendationType.RECEIVED, scrapeTab(webDriver, RECEIVED_RECOMMENDATIONS_TAB)));
        if (isNull(findElementBy(webDriver, ZERO_GIVEN_RECOMMENDATIONS)))
            recommendationsSources.add(new RecommendationsSource(RecommendationType.GIVEN, scrapeTab(webDriver, GIVEN_RECOMMENDATIONS_TAB)));
        return recommendationsSources;
    }

    private String scrapeTab(WebDriver webDriver, By receivedRecommendationsTab) {
        WebDriverWait wait = new WebDriverWait(webDriver, 45);
        Actions actions = new Actions(webDriver);
        openRecommendationsTab(webDriver, actions, receivedRecommendationsTab);
        openAllRecommendations(webDriver, wait, actions);
        clickAllSeeMoreButton(webDriver, actions);
        return getElementHtml(findElementBy(webDriver, RECOMMENDATION_SECTION));
    }

    private void openAllRecommendations(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        WebElement showMoreRecommendationsButton = findElementBy(webDriver, SHOW_MORE_RECOMMENDATIONS_BUTTON);
        while (nonNull(showMoreRecommendationsButton)) {
            scrollToAndClickOnElement(webDriver, actions, showMoreRecommendationsButton);
            randomSleep(4500, 6000);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_ANIMATION));
            showMoreRecommendationsButton = findElementBy(webDriver, SHOW_MORE_RECOMMENDATIONS_BUTTON);
        }
    }

    private void openRecommendationsTab(WebDriver webDriver, Actions actions, By recommendationsTabSelector) {
        WebElement recommendationsTab = findWebElementBy(webDriver, recommendationsTabSelector)
                .orElseThrow(() -> ElementNotFoundException.create("Recommendations tab"));
        scrollToAndClickOnElement(webDriver, actions, recommendationsTab);
        randomSleep(2500, 4500);
    }

    private void clickAllSeeMoreButton(WebDriver webDriver, Actions actions) {
        List<WebElement> allSeeMoreButtons = webDriver.findElements(SEE_MORE_BUTTON);
        if (allSeeMoreButtons.isEmpty())
            return;
        WebElement firstSeeMoreButton = allSeeMoreButtons.get(0);
        scrollToElement(webDriver, firstSeeMoreButton, 450);
        for (WebElement seeMoreButton : allSeeMoreButtons) {
            scrollToAndClickOnElement(webDriver, actions, seeMoreButton);
            randomSleep(1500, 2500);
        }
    }
}
