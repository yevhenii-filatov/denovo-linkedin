package com.dataox.googleserp.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dmitriy Lysko
 * @since 05/04/2021
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.service")
public class ServiceProperties {
    private String loadBalancerUrl;
}
