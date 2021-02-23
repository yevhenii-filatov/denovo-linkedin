package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

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

import static com.dataox.CommonUtils.randomLong;
import static com.dataox.CommonUtils.randomSleep;
import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Slf4j
@Service
public class HeaderSectionScraper implements Scraper<String> {

    private static final By HEADER_SELECTOR = By.xpath("//section[contains(@class,'pv-top-card')]");
    private static final By CROPPED_PROFILE_PHOTO = By.xpath("//img[contains(@class,'pv-top-card__photo')][not(contains(@class,'ghost-person'))]");
    private static final By FULL_PROFILE_PHOTO = By.xpath("//img[starts-with(@alt,'Profile photo of')]");
    private static final By CLOSE_POPUP_BUTTON = By.xpath("//button[@aria-label='Dismiss']");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement headerSection = findWebElementBy(webDriver, HEADER_SELECTOR)
                .orElseThrow(() -> ElementNotFoundException.notFound("Header section"));
        log.info("Scraping header section");
        return getElementHtml(headerSection);
    }

    public String scrapeProfilePhotoUrl(WebDriver webDriver) {
        randomSleep(3500, 5000);
        WebElement croppedProfilePhoto = findElementBy(webDriver, CROPPED_PROFILE_PHOTO);
        if (isNull(croppedProfilePhoto)) {
            log.info("Profile photo is not present");
            return "";
        }
        log.info("Scraping profile photo");
        Actions actions = new Actions(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        clickOnElement(croppedProfilePhoto, actions);
        wait.until(ExpectedConditions.presenceOfElementLocated(FULL_PROFILE_PHOTO));
        WebElement fullProfilePhoto = findWebElementBy(webDriver, FULL_PROFILE_PHOTO)
                .orElseThrow(() -> ElementNotFoundException.notFound("Full profile photo"));
        String profilePhotoUrl = fullProfilePhoto.getAttribute("src");
        randomSleep(3500, 5000);
        closePopUp(actions, webDriver);
        return profilePhotoUrl;
    }

    private void closePopUp(Actions actions, WebDriver webDriver) {
        WebElement closeButton = findWebElementBy(webDriver, CLOSE_POPUP_BUTTON)
                .orElseThrow(() -> ElementNotFoundException.notFound("Close full profile photo button"));
        clickOnElement(closeButton, actions, randomLong(750, 1500));
    }
}
