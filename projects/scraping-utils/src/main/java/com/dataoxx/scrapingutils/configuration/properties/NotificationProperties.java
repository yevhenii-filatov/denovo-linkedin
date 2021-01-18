package com.dataoxx.scrapingutils.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "app.notification")
public class NotificationProperties {
    private Telegram telegram;

    @Data
    public static class Telegram {
        private String botToken;
        private String channelId;
        private Boolean enabled;
    }
}
