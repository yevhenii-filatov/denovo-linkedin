package com.dataox.linkedinscraper.scraping.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 11/02/2021
 */
@Data
@Configuration
@ConfigurationProperties("app.scraper")
public class ScraperProperties {
    private Integer activitiesAmount;
}
