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
@ConfigurationProperties(prefix = "app.proxy")
public class ProxyProperties {
    private OperiaProperties operia;

    @Data
    public static class OperiaProperties {
        private String key;
    }
}
