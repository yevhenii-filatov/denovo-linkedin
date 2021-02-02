package com.dataox.linkedinscraper.scraping.scrapers.subscrapers;

import com.dataox.WebDriverUtils;
import com.dataox.linkedinscraper.dto.types.RecommendationType;
import com.dataox.linkedinscraper.scraping.scrapers.Scraper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.dataox.WebDriverUtils.*;
import static java.util.Objects.isNull;

/**
 * @author Dmitriy Lysko
 * @since 29/01/2021
 */
@Service
public class RecommendationsScraper implements Scraper<Map<String,String>> {

    private static final By RECOMMENDATION_SECTION = By.xpath("//section[contains(@class,'pv-profile-section pv-recommendations-section')]");

    @Override
    public Map<String, String> scrape(WebDriver webDriver) {
        WebElement recommendationSection = findElementBy(webDriver, RECOMMENDATION_SECTION);
        if (isNull(recommendationSection))
            return Collections.emptyMap();
        else
            scrollToElement(webDriver,recommendationSection,200);
        Map<String, String> recommendations = new HashMap<>();
        recommendations.put(RecommendationType.RECEIVED, "asd");
        recommendations.put(RecommendationType.GIVEN, "asd");
        return recommendations;
    }
}
