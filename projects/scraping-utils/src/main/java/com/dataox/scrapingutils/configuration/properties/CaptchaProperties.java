package com.dataox.scrapingutils.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yevhenii Filatov
 * @since 1/15/21
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "app.captcha")
public class CaptchaProperties {
    private AntiCaptcha antiCaptcha;

    @Data
    public static class AntiCaptcha {
        private String token;
    }
}