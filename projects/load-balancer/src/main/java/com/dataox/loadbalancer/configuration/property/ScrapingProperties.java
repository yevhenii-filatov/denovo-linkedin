package com.dataox.loadbalancer.configuration.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.scraping")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScrapingProperties {
    int batchSize;
}
