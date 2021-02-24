package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.configuration.property.ScraperProperties;
import com.dataox.linkedinscraper.scraping.exceptions.ElementNotFoundException;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.RequiredArgsConstructor;
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
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitiesScraper implements Scraper<List<String>> {

    private final ScraperProperties scraperProperties;
    private static final By EMPTY_ACTIVITIES = By.xpath("//h1[text()='Nothing to see for now']");
    private static final By ACTIVITY_POSTS = By.xpath("//div[contains(@class,'pv-recent-activity-detail__feed-container')]" +
            "/div[contains(@class,'occludable-update')]");
    private static final By LOADING_ANIMATION = By.xpath("//div[contains(@class,'pv-recent-activity-detail__feed-container')]" +
            "/div[contains(@class,'detail-page-loader-container')]");
    private static final By COPY_LINK_BUTTON = By.xpath("//h5[text()='Copy link to post']");
    private static final By SEE_ALL_ACTIVITIES_BUTTON = By.xpath("//section[contains(@class,'pv-recent-activity-section-v2')]" +
            "/a/span[text()='See all activity'][1]");
    private static final By LINK_COPIED_NOTIFICATION = By.xpath("//li[@data-test-artdeco-toast-item-type='success']");
    private static final By POST_LINK = By.xpath("//li[@data-test-artdeco-toast-item-type='success']//a");

    @Override
    public List<String> scrape(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        wait.until(ExpectedConditions.presenceOfElementLocated(SEE_ALL_ACTIVITIES_BUTTON));
        WebElement seeAllButton = findWebElementBy(webDriver, SEE_ALL_ACTIVITIES_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.notFound("See all activity button"));
        log.info("Scraping activities");
        Actions action = new Actions(webDriver);
        scrollToAndClickOnElement(webDriver, action, seeAllButton);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(ACTIVITY_POSTS),
                ExpectedConditions.presenceOfElementLocated(EMPTY_ACTIVITIES)
        ));
        WebElement emptyActivitiesMessage = findElementBy(webDriver, EMPTY_ACTIVITIES);
        if (nonNull(emptyActivitiesMessage))
            return emptyList();
        return collectActivities(webDriver, wait);
    }

    private List<String> collectActivities(WebDriver webDriver, WebDriverWait wait) {
        List<String> activitiesSources = new ArrayList<>();
        WebElement currentPost;

        for (int i = 0; i < scraperProperties.getActivitiesAmount(); i++) {
            List<WebElement> posts = webDriver.findElements(ACTIVITY_POSTS);
            checkIsPostsEmpty(posts);
            if (posts.size() - 1 < i)
                return activitiesSources;
            currentPost = posts.get(i);
            scrollToElement(webDriver, currentPost, 100);
            activitiesSources.add(getElementHtml(currentPost));
            randomSleep(300, 500);
        }
        return activitiesSources;
    }

    private void checkIsPostsEmpty(List<WebElement> posts) {
        if (posts.isEmpty())
            throw ElementNotFoundException.notFound("Activity posts");
    }

}
