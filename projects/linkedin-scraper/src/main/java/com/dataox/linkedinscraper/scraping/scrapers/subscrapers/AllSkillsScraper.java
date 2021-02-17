package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.CommonUtils;
import com.dataox.WebDriverUtils;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.*;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class AllSkillsScraper implements Scraper<String> {
    private static final By SHOW_MORE_SKILLS = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]" +
            "//button[@data-control-name='skill_details'][@aria-expanded='false']");
    private static final By SKILLS_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-skill-categories-section')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement showMoreSkillsButton = findElementBy(webDriver, SHOW_MORE_SKILLS);
        if (nonNull(showMoreSkillsButton)) {
            scrollToElement(webDriver, showMoreSkillsButton, 200);
            new Actions(webDriver).moveToElement(showMoreSkillsButton).pause(randomLong(1500, 2000)).click().perform();
        }
        WebElement skillsSection = findElementBy(webDriver, SKILLS_SECTION);
        if (isNull(skillsSection))
            return "";
        return getElementHtml(skillsSection);
    }
}
