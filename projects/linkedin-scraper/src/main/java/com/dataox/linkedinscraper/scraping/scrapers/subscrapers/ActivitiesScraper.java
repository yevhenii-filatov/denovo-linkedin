package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.configuration.property.ScraperProperties;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitiesScraper implements Scraper<Map<String, String>> {

    private final ScraperProperties scraperProperties;
    private static final By EMPTY_ACTIVITIES = By.xpath("//h1[text()='Nothing to see for now']");
    private static final By ACTIVITY_POSTS = By.xpath("//div[contains(@class,'pv-recent-activity-detail__feed-container')]/div");
    private static final By SEE_ALL_ACTIVITIES_BUTTON = By.xpath("//section[contains(@class,'pv-recent-activity-section-v2')]" +
            "/a/span[text()='See all activity'][1]");
    private static final By POSTS_MENU_BUTTON = By.className("feed-shared-control-menu__trigger");
    private static final By COPY_LINK_BUTTON = By.xpath("//h5[text()='Copy link to post']");

    @Override
    public Map<String, String> scrape(WebDriver webDriver) {
        WebElement seeAllButton = findElementBy(webDriver, SEE_ALL_ACTIVITIES_BUTTON);
        if (isNull(seeAllButton))
            return Collections.emptyMap();
        Actions action = new Actions(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 45);
        scrollToAndClickOnElement(webDriver,action,seeAllButton);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(ACTIVITY_POSTS),
                ExpectedConditions.presenceOfElementLocated(EMPTY_ACTIVITIES)
        ));
        if (nonNull(findElementBy(webDriver, EMPTY_ACTIVITIES)))
            return Collections.emptyMap();
        return collectActivities(webDriver, action, wait);
    }

    private Map<String, String> collectActivities(WebDriver webDriver, Actions action, WebDriverWait wait) {
        Map<String, String> urlAndActivityPost = new HashMap<>();
        WebElement currentPost;

        for (int i = 0; i < scraperProperties.getActivitiesAmount(); i++) {
            List<WebElement> posts = webDriver.findElements(ACTIVITY_POSTS);
            if (posts.size()-1 < i)
                return urlAndActivityPost;
            currentPost = posts.get(i);
            scrollToElement(webDriver, currentPost, 300);
            String postUrl = getPostUrl(action, wait, currentPost);
            urlAndActivityPost.put(postUrl, getElementHtml(currentPost));
            randomSleep(1500, 2500);
        }
        return urlAndActivityPost;
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
