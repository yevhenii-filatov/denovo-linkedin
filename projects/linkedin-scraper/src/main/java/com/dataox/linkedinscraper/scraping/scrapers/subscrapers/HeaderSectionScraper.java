package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class HeaderSectionScraper implements Scraper<String> {

    private final By HEADER_SELECTOR = By.xpath("//section[contains(@class,'pv-top-card')]");
    private final By CROPPED_PROFILE_PHOTO = By.xpath("//img[contains(@class,'pv-top-card__photo')][not(contains(@class,'ghost-person'))]");
    private final By FULL_PROFILE_PHOTO = By.xpath("//img[starts-with(@alt,'Profile photo of')]");
    private final By CLOSE_POPUP_BUTTON = By.xpath("//button[@aria-label='Dismiss']");

    @Override
    public String scrape(WebDriver webDriver) {
        return getElementHtml(findElementBy(webDriver, HEADER_SELECTOR));
    }

    public String scrapeProfilePhotoUrl(WebDriver webDriver) {
        randomSleep(3500,5000);
        WebElement croppedProfilePhoto = findElementBy(webDriver, CROPPED_PROFILE_PHOTO);
        if (isNull(croppedProfilePhoto))
            return "";
        Actions actions = new Actions(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        clickOnElement(croppedProfilePhoto,actions,randomLong(750,1500));
        wait.until(ExpectedConditions.presenceOfElementLocated(FULL_PROFILE_PHOTO));
        String profilePhotoUrl = findElementBy(webDriver, FULL_PROFILE_PHOTO).getAttribute("src");
        randomSleep(3500,5000);
        closePopUp(actions, webDriver);
        return profilePhotoUrl;
    }

    private void closePopUp(Actions actions, WebDriver webDriver) {
        WebElement closeButton = findElementBy(webDriver, CLOSE_POPUP_BUTTON);
        clickOnElement(closeButton,actions,randomLong(750,1500));
    }
}
