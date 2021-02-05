package com.dataox.googleserp.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "app.search")
public class SearchProperties {
    private Long tasksTimeOut;
    private Integer retryAttempts;
    private Integer concurrencyRestriction;
    private String userAgent;
    private Integer requiredResultsCount;
}
