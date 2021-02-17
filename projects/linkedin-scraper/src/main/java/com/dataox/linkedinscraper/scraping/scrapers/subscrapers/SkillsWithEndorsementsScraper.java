package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 02/02/2021
 */
@Service
public class SkillsWithEndorsementsScraper implements Scraper<Map<String, List<String>>> {

    private static final int SCROLL_STEP = 500;
    private static final String TOP_CATEGORY_NAME = "Top";
    private static final By SKILLS_POPUP_WINDOW = By.xpath("//div[@role='dialog']");
    private static final By CLOSE_POPUP_SKILLS_WINDOW_BUTTON = By.xpath("//div[@role='dialog']/button[@aria-label='Dismiss']");
    private static final By SKILLS_CATEGORY_SECTIONS = By.xpath("//div[contains(@class,'pv-skill-category-list')]");
    private static final By CATEGORY_NAME = By.tagName("h3");
    private static final By ENDORSEMENTS_IN_CATEGORY_BUTTON = By.className("pv-skill-category-entity__endorsement-count");
    private static final By TOP_CATEGORY_ENDORSEMENTS_BUTTONS = By.xpath("//ol[contains(@class,'pv-skill-categories-section__top-skills')]" +
            "//span[contains(@class,'pv-skill-category-entity__endorsement-count')]");
    private static final By SKILLS_WITH_ENDORSEMENTS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//ol/li//a[@data-control-name='skills_endorsement_full_list']/span[contains(@class,'pv-skill-category-entity__endorsement-count')]");
    private static final By SHOW_MORE_SKILLS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//button[@data-control-name='skill_details']");
    private static final String POPUP_SCROLLING_AREA = "//div[@data-test-modal]//div[@class='artdeco-modal__content ember-view']";

    @Override
    public Map<String, List<String>> scrape(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        WebElement showMoreSkillsButton = findElementBy(webDriver, SHOW_MORE_SKILLS);
        if (nonNull(showMoreSkillsButton))
            scrollToAndClickOnElement(webDriver, showMoreSkillsButton);
        List<WebElement> skillsWithEndorsements = webDriver.findElements(SKILLS_WITH_ENDORSEMENTS);
        if (skillsWithEndorsements.isEmpty())
            return Collections.emptyMap();
        scrollToElement(webDriver, skillsWithEndorsements.get(0), 400);
        return collectSkillsSources(webDriver, wait);
    }

    private Map<String, List<String>> collectSkillsSources(WebDriver webDriver, WebDriverWait wait) {
        Map<String, List<String>> skillsSources = new HashMap<>();
        skillsSources.putAll(collectTopCategory(webDriver, wait));
        skillsSources.putAll(collectOtherCategories(webDriver, wait));
        return skillsSources;
    }

    private Map<String, ? extends List<String>> collectTopCategory(WebDriver webDriver, WebDriverWait wait) {
        Map<String, List<String>> topCategorySkills = new HashMap<>();
        List<String> sources = new ArrayList<>();
        for (WebElement endorsementButton : webDriver.findElements(TOP_CATEGORY_ENDORSEMENTS_BUTTONS)) {
            collectEndorsementsSource(webDriver, wait, sources, endorsementButton);
        }
        topCategorySkills.put(TOP_CATEGORY_NAME, sources);
        return topCategorySkills;
    }

    private Map<String, ? extends List<String>> collectOtherCategories(WebDriver webDriver, WebDriverWait wait) {
        Map<String, List<String>> skillsByCategories = new HashMap<>();
        String category;
        for (WebElement skillCategorySection : webDriver.findElements(SKILLS_CATEGORY_SECTIONS)) {
            category = skillCategorySection.findElement(CATEGORY_NAME).getText();
            List<String> sources = new ArrayList<>();
            for (WebElement endorsementsButton : skillCategorySection.findElements(ENDORSEMENTS_IN_CATEGORY_BUTTON)) {
                collectEndorsementsSource(webDriver, wait, sources, endorsementsButton);
            }
            skillsByCategories.put(category, sources);
        }
        return skillsByCategories;
    }

    private void collectEndorsementsSource(WebDriver webDriver, WebDriverWait wait, List<String> sources, WebElement endorsementButton) {
        scrollToAndClickOnElement(webDriver, endorsementButton);
        wait.until(ExpectedConditions.elementToBeClickable(CLOSE_POPUP_SKILLS_WINDOW_BUTTON));
        sources.add(getElementHtml(findElementBy(webDriver, SKILLS_POPUP_WINDOW)));
        randomSleep(1500, 2000);
        scrollToTheBottomOfPopUp(webDriver);
        WebElement closeButton = findElementBy(webDriver, CLOSE_POPUP_SKILLS_WINDOW_BUTTON);
        new Actions(webDriver).moveToElement(closeButton).pause(randomLong(1000, 1500)).click().perform();
        randomSleep(750, 1500);
    }

    private void scrollToTheBottomOfPopUp(WebDriver webDriver) {
        int desiredScrollY = 0;
        WebElement scrollingArea = findElementBy(webDriver, By.xpath(POPUP_SCROLLING_AREA));
        Long beforeScroll;
        Long afterScroll;
        do {
            beforeScroll = getCurrentScrollYInElement(webDriver, scrollingArea);
            executeJavascript(webDriver, "arguments[0].scrollTop=arguments[1]", scrollingArea, desiredScrollY += SCROLL_STEP);
            afterScroll = getCurrentScrollYInElement(webDriver, scrollingArea);
            randomSleep(750, 1500);
        } while (!beforeScroll.equals(afterScroll));
    }

    private Long getCurrentScrollYInElement(WebDriver webDriver, WebElement scrollingArea) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        return (Long) js.executeScript("return arguments[0].scrollTop", scrollingArea);
    }

    private void scrollToAndClickOnElement(WebDriver webDriver, WebElement webElement) {
        scrollToElement(webDriver, webElement, 400);
        new Actions(webDriver).moveToElement(webElement).pause(randomLong(750, 1500)).click().perform();
        randomSleep(750, 2000);
    }
}
