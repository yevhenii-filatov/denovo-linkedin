package com.dataox.linkedinscraper.scraping.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Viacheslav_Yakovenko
 * @since 24.06.2021
 */
@Data
@Configuration
@ConfigurationProperties("app.query")
public class QueryProperties {
    private String token;
}
