package com.dataox.linkedinscraper.scraping.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 01/03/2021
 */
@Data
@Configuration
@ConfigurationProperties("app.linkedin")
public class LinkedinProperties {
    private String profileLogin;
    private String profilePassword;
}
