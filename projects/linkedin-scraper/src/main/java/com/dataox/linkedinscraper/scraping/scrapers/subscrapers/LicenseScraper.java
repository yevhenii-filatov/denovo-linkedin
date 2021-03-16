package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

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
public class LicenseScraper implements Scraper<String> {

    private static final By CERTIFICATES_SECTION = By.xpath("//section[@id='certifications-section']");
    private static final By SHOW_MORE_CERTIFICATES = By.xpath("//section[@id='certifications-section']//button[starts-with(text(),'Show')]");

    @Override
    public String scrape(WebDriver webDriver) {
        WebElement certificatesSection = findElementBy(webDriver, CERTIFICATES_SECTION);
        if (isNull(certificatesSection)) {
            log.info("Certificates section is not present");
            return "";
        }
        log.info("Scraping license section");
        scrollToElement(webDriver, certificatesSection, 300);
        randomSleep(2000, 5000);

        showAllCertificates(webDriver);
        certificatesSection = findElementBy(webDriver, CERTIFICATES_SECTION);
        return getElementHtml(certificatesSection);
    }

    private void showAllCertificates(WebDriver webDriver) {
        WebElement showMoreCertificatesButton = findElementBy(webDriver, SHOW_MORE_CERTIFICATES);
        Actions actions = new Actions(webDriver);
        while (nonNull(showMoreCertificatesButton)) {
            WebElement showMoreButton = findElementBy(webDriver, SHOW_MORE_CERTIFICATES);
            scrollToAndClickOnElement(webDriver, actions, showMoreButton);
            randomSleep(2500, 5000);
            showMoreCertificatesButton = findElementBy(webDriver, SHOW_MORE_CERTIFICATES);
        }
    }

}
