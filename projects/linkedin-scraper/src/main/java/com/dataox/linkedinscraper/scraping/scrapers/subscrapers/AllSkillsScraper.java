package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
public class AllSkillsScraper implements Scraper<String> {
    private static final By SHOW_MORE_SKILLS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//button[@data-control-name='skill_details'][@aria-expanded='false']");
    private static final By SKILLS_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement skillsSection = findElementBy(webDriver, SKILLS_SECTION);
        if (isNull(skillsSection)) {
            log.info("Skills section is not present");
            return "";
        }
        log.info("Scraping all skills section");
        findWebElementBy(webDriver, SHOW_MORE_SKILLS)
                .ifPresent(webElement -> clickShowMoreSkills(webDriver, webElement));
        return getElementHtml(skillsSection);
    }

    private void clickShowMoreSkills(WebDriver webDriver, WebElement showMoreSkillsButton) {
        Actions actions = new Actions(webDriver);
        scrollToAndClickOnElement(webDriver, actions, showMoreSkillsButton);
    }
}
