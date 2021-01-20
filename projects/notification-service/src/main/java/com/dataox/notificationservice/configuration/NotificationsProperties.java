package com.dataox.notificationservice.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.notifications")
public class NotificationsProperties {
    private Telegram telegram;
    private Slack slack;

    @Data
    public static class Telegram {
        private String botToken;
        private String chatId;
    }

    @Data
    public static class Slack {
        private String slackToken;
        private String notificationsChannel;
    }
}
