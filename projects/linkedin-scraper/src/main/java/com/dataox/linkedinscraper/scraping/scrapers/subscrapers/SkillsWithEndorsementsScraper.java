package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.dto.sources.SkillsSource;
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

/**
 * @author Dmitriy Lysko
 * @since 02/02/2021
 */
@Slf4j
@Service
public class SkillsWithEndorsementsScraper implements Scraper<List<SkillsSource>> {

    private static final int SCROLL_STEP = 500;
    private static final String TOP_CATEGORY_NAME = "Top";
    private static final By SKILLS_POPUP_WINDOW = By.xpath("//div[@role='dialog']");
    private static final By CLOSE_POPUP_SKILL_ENDORSEMENTS_BUTTON = By.xpath("//div[@role='dialog']/button[@aria-label='Dismiss']");
    private static final By SKILLS_CATEGORY_SECTIONS = By.xpath("//div[contains(@class,'pv-skill-category-list')]");
    private static final By CATEGORY_NAME = By.tagName("h3");
    private static final By ENDORSEMENTS_IN_CATEGORY_BUTTON = By.className("pv-skill-category-entity__endorsement-count");
    private static final By TOP_CATEGORY_ENDORSEMENTS_BUTTONS = By.xpath("//ol[contains(@class,'pv-skill-categories-section__top-skills')]" +
            "//span[contains(@class,'pv-skill-category-entity__endorsement-count')]");
    private static final By SKILLS_WITH_ENDORSEMENTS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//ol/li//a[@data-control-name='skills_endorsement_full_list']/span[contains(@class,'pv-skill-category-entity__endorsement-count')]");
    private static final By SHOW_MORE_SKILLS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//button[@data-control-name='skill_details']");
    private static final By POPUP_SCROLLING_AREA = By.xpath("//div[@data-test-modal]//div[@class='artdeco-modal__content ember-view']");

    @Override
    public List<SkillsSource> scrape(WebDriver webDriver) {
        Actions actions = new Actions(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        findWebElementBy(webDriver, SHOW_MORE_SKILLS)
                .ifPresent(webElement -> scrollToAndClickOnElement(webDriver, actions, webElement));
        List<WebElement> skillsWithEndorsements = webDriver.findElements(SKILLS_WITH_ENDORSEMENTS);
        if (skillsWithEndorsements.isEmpty()) {
            log.info("Skills with endorsements is not present");
            return Collections.emptyList();
        }
        log.info("Scraping skills with endorsements section");
        scrollToElement(webDriver, skillsWithEndorsements.get(0), 400);
        return collectSkillsSources(webDriver, wait, actions);
    }

    private List<SkillsSource> collectSkillsSources(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        List<SkillsSource> skillsSources = new ArrayList<>();
        skillsSources.add(collectTopCategory(webDriver, wait, actions));
        skillsSources.addAll(collectOtherCategories(webDriver, wait, actions));
        return skillsSources;
    }

    private SkillsSource collectTopCategory(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        List<String> sources = new ArrayList<>();
        List<WebElement> topCategoryEndorsements = webDriver.findElements(TOP_CATEGORY_ENDORSEMENTS_BUTTONS);
        for (WebElement endorsementButton : topCategoryEndorsements)
            sources.addAll(collectEndorsementsSource(webDriver, wait, endorsementButton, actions));
        return new SkillsSource(TOP_CATEGORY_NAME, sources);
    }

    private List<SkillsSource> collectOtherCategories(WebDriver webDriver, WebDriverWait wait, Actions actions) {
        List<SkillsSource> skillsSources = new ArrayList<>();
        List<WebElement> skillsCategorySection = webDriver.findElements(SKILLS_CATEGORY_SECTIONS);
        for (WebElement skillCategorySection : skillsCategorySection)
            if (!skillCategorySection.findElements(ENDORSEMENTS_IN_CATEGORY_BUTTON).isEmpty())
                skillsSources.add(collectSkillsFromCategory(webDriver, wait, actions, skillCategorySection));
        return skillsSources;
    }

    private SkillsSource collectSkillsFromCategory(WebDriver webDriver, WebDriverWait wait, Actions actions, WebElement skillCategorySection) {
        String category = findWebElementFromParentBy(skillCategorySection, CATEGORY_NAME)
                .orElseThrow(() -> ElementNotFoundException.notFound("Category name"))
                .getText();
        List<String> sources = new ArrayList<>();
        List<WebElement> endorsementsInCategory = skillCategorySection.findElements(ENDORSEMENTS_IN_CATEGORY_BUTTON);
        for (WebElement endorsementsButton : endorsementsInCategory)
            sources.addAll(collectEndorsementsSource(webDriver, wait, endorsementsButton, actions));
        return new SkillsSource(category, sources);
    }

    private List<String> collectEndorsementsSource(WebDriver webDriver,
                                                   WebDriverWait wait,
                                                   WebElement endorsementButton,
                                                   Actions actions) {
        List<String> sources = new ArrayList<>();
        scrollToAndClickOnElement(webDriver, actions, endorsementButton);
        wait.until(ExpectedConditions.elementToBeClickable(CLOSE_POPUP_SKILL_ENDORSEMENTS_BUTTON));
        sources.add(getElementHtml(findElementBy(webDriver, SKILLS_POPUP_WINDOW)));
        randomSleep(1500, 2000);
        WebElement scrollingArea = findWebElementBy(webDriver, POPUP_SCROLLING_AREA)
                .orElseThrow(() -> ElementNotFoundException.notFound("Endorsements popup scrolling area"));
        scrollToTheBottomOfElement(webDriver, scrollingArea, SCROLL_STEP);
        WebElement closeButton = findWebElementBy(webDriver, CLOSE_POPUP_SKILL_ENDORSEMENTS_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.notFound("Close popup skill endorsements button"));
        clickOnElement(closeButton, actions);
        randomSleep(750, 1500);
        return sources;
    }

}
