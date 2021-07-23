package com.dataox.loadbalancer.configuration.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 05/04/2021
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.search")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleSearchProperties {
    String serviceUrl;
}
