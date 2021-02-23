package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.sources.ActivitiesSource;
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

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitiesScraper implements Scraper<List<ActivitiesSource>> {

    private final ScraperProperties scraperProperties;
    private static final By EMPTY_ACTIVITIES = By.xpath("//h1[text()='Nothing to see for now']");
    private static final By ACTIVITY_POSTS = By.xpath("//div[contains(@class,'pv-recent-activity-detail__feed-container')]/div");
    private static final By POSTS_MENU_BUTTON = By.className("feed-shared-control-menu__trigger");
    private static final By COPY_LINK_BUTTON = By.xpath("//h5[text()='Copy link to post']");
    private static final By SEE_ALL_ACTIVITIES_BUTTON = By.xpath("//section[contains(@class,'pv-recent-activity-section-v2')]" +
            "/a/span[text()='See all activity'][1]");

    @Override
    public List<ActivitiesSource> scrape(WebDriver webDriver) {
        WebElement seeAllButton = findWebElementBy(webDriver, SEE_ALL_ACTIVITIES_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.notFound("See all activity button"));
        Actions action = new Actions(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        scrollToAndClickOnElement(webDriver, action, seeAllButton);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(ACTIVITY_POSTS),
                ExpectedConditions.presenceOfElementLocated(EMPTY_ACTIVITIES)
        ));
        WebElement emptyActivitiesMessage = findElementBy(webDriver, EMPTY_ACTIVITIES);
        if (nonNull(emptyActivitiesMessage))
            return emptyList();
        return collectActivities(webDriver, action, wait);
    }

    private List<ActivitiesSource> collectActivities(WebDriver webDriver, Actions action, WebDriverWait wait) {
        List<ActivitiesSource> activitiesSources = new ArrayList<>();
        WebElement currentPost;

        for (int i = 0; i < scraperProperties.getActivitiesAmount(); i++) {
            List<WebElement> posts = webDriver.findElements(ACTIVITY_POSTS);
            checkIsPostsEmpty(posts);
            if (posts.size() - 1 < i)
                return activitiesSources;
            currentPost = posts.get(i);
            scrollToElement(webDriver, currentPost, 300);
            String postUrl = getPostUrl(action, wait, currentPost);
            activitiesSources.add(new ActivitiesSource(postUrl, getElementHtml(currentPost)));
            randomSleep(1500, 2500);
        }
        return activitiesSources;
    }

    private void checkIsPostsEmpty(List<WebElement> posts) {
        if (posts.isEmpty())
            throw ElementNotFoundException.notFound("Activity posts");
    }

    private String getPostUrl(Actions action, WebDriverWait wait, WebElement currentPost) {
        WebElement postMenu = currentPost.findElement(POSTS_MENU_BUTTON);
        clickOnElement(postMenu, action, randomLong(750, 1500));
        wait.until(ExpectedConditions.presenceOfElementLocated(COPY_LINK_BUTTON));
        WebElement copyLinkButton = currentPost.findElement(COPY_LINK_BUTTON);
        clickOnElement(copyLinkButton, action, randomLong(750, 1500));
        return fetchPostUrlFromClipboard();
    }

    private String fetchPostUrlFromClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new NullPointerException("Can't retrieve url from clipboard");
        }
    }
}