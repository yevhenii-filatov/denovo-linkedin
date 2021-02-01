package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.WebDriverUtils.*;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
public class ExperienceScraper implements Scraper<String> {

    private static final By EXPERIENCE_SECTION = By.xpath("//section[@id='experience-section']");
    private static final By SEE_MORE_BUTTONS = By.xpath("//section[@id='experience-section']//ul/li//button[text()='see more']");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement experienceSection = findElementBy(webDriver, EXPERIENCE_SECTION);
        scrollToElement(webDriver, experienceSection, 200);
        Actions actions = new Actions(webDriver);
        for (WebElement seeMoreButton : webDriver.findElements(SEE_MORE_BUTTONS))
            actions.moveToElement(seeMoreButton).pause(randomLong(1000, 2000)).click().perform();
        experienceSection = findElementBy(webDriver, EXPERIENCE_SECTION);
        return getElementHtml(experienceSection);
    }
}
