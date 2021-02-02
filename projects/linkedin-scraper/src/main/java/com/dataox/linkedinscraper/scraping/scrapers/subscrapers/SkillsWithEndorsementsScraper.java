package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
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

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.ScrollingDirection.UP;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 02/02/2021
 */
@Service
public class SkillsWithEndorsementsScraper implements Scraper<List<String>> {

    private static final By SKILLS_POPUP_WINDOW = By.xpath("//div[@role='dialog']");
    private static final By CLOSE_POPUP_SKILLS_WINDOW_BUTTON = By.xpath("//div[@role='dialog']/button[@aria-label='Dismiss']");
    private static final By SKILLS_WITH_ENDORSEMENTS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//ol/li//a[@data-control-name='skills_endorsement_full_list']/span[contains(@class,'pv-skill-category-entity__endorsement-count')]");
    private static final By SHOW_MORE_SKILLS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//button[@data-control-name='skill_details']");

    @Override
    public List<String> scrape(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        WebElement showMoreSkillsButton = findElementBy(webDriver, SHOW_MORE_SKILLS);
        if (nonNull(showMoreSkillsButton))
            scrollToAndClickOnElement(webDriver, showMoreSkillsButton);
        WebElement firstSkill = webDriver.findElements(SKILLS_WITH_ENDORSEMENTS).get(0);
        if (isNull(firstSkill))
            return Collections.emptyList();
        scrollToFirstSkill(firstSkill, webDriver);
        return collectSkillsSources(webDriver, wait);
    }

    private List<String> collectSkillsSources(WebDriver webDriver, WebDriverWait wait) {
        List<String> skillSourcesWithEndorsements = new ArrayList<>();
        Actions actions = new Actions(webDriver);
        for (WebElement skill : webDriver.findElements(SKILLS_WITH_ENDORSEMENTS)) {
            scrollToElement(webDriver, skill,400);
            actions.moveToElement(skill).pause(randomLong(1500,2500)).click().perform();
            wait.until(ExpectedConditions.presenceOfElementLocated(CLOSE_POPUP_SKILLS_WINDOW_BUTTON));
            randomSleep(2000,5000);
            skillSourcesWithEndorsements.add(getElementHtml(findElementBy(webDriver, SKILLS_POPUP_WINDOW)));
            actions.moveToElement(findElementBy(webDriver,CLOSE_POPUP_SKILLS_WINDOW_BUTTON)).pause(randomLong(800,1500)).click().perform();
            randomSleep(3000,6000);
        }
        return skillSourcesWithEndorsements;
    }

    private void scrollToAndClickOnElement(WebDriver webDriver, WebElement showMoreSkillsButton) {
        scrollToElement(webDriver, showMoreSkillsButton, 200);
        new Actions(webDriver).moveToElement(showMoreSkillsButton).pause(randomLong(1000, 2500)).click().perform();
        randomSleep(1500, 3500);
    }

    private void scrollToFirstSkill(WebElement firstSkill, WebDriver webDriver) {
        int firstSkillY = firstSkill.getLocation().y;
        int headerPanelSizeY = 400;
        firstSkillY -= headerPanelSizeY;
        Long currentScrollY = getScrollY(webDriver);
        int amountOfSteps = 25;
        long step = (currentScrollY - firstSkillY) / amountOfSteps;
        for (int i = 0; i < amountOfSteps; i++) {
            scroll(webDriver, UP, step);
            randomSleep(200, 400);
        }
    }
}
