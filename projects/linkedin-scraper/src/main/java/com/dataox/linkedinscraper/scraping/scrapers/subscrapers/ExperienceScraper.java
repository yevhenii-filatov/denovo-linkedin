package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static com.dataox.WebDriverUtils.ScrollingDirection.UP;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class ExperienceScraper implements Scraper<String> {

    private static final By EXPERIENCE_SECTION = By.xpath("//section[@id='experience-section']");
    private static final By SHOW_MORE_EXPERIENCES_BUTTON = By.xpath("//section[@id='experience-section']//button[contains(text(),'more experience')]");
    private static final By SEE_MORE_BUTTON = By.xpath("//section[@id='experience-section']//ul/li//button[text()='see more']");

    @Override
    public String scrape(WebDriver webDriver) {
        randomSleep(2000, 5000);
        WebElement experienceSection = findElementBy(webDriver, EXPERIENCE_SECTION);
        scrollToElement(webDriver, experienceSection, 400);
        Actions actions = new Actions(webDriver);
        while (nonNull(findElementBy(webDriver, SHOW_MORE_EXPERIENCES_BUTTON))) {
            scrollToAndClickSeeMoreExperiencesButton(webDriver, actions);
            randomSleep(2500, 4500);
        }
        clickAllSeeMoreButtons(webDriver, actions);
        experienceSection = findElementBy(webDriver, EXPERIENCE_SECTION);
        return getElementHtml(experienceSection);
    }

    private void scrollToAndClickSeeMoreExperiencesButton(WebDriver webDriver, Actions actions) {
        WebElement showMoreExperiencesButton = findElementBy(webDriver, SHOW_MORE_EXPERIENCES_BUTTON);
        scrollToElement(webDriver, showMoreExperiencesButton, 200);
        actions.moveToElement(showMoreExperiencesButton).pause(randomLong(1500, 2500)).click().perform();
    }

    private void clickAllSeeMoreButtons(WebDriver webDriver, Actions actions) {
        WebElement firstButton = webDriver.findElements(SEE_MORE_BUTTON).get(0);
        scrollToElement(webDriver,firstButton,500);
        for (WebElement seeMoreButton : webDriver.findElements(SEE_MORE_BUTTON)) {
            scrollToElement(webDriver, seeMoreButton, 500);
            actions.moveToElement(seeMoreButton).pause(randomLong(1000, 2000)).click().perform();
        }
    }


}
