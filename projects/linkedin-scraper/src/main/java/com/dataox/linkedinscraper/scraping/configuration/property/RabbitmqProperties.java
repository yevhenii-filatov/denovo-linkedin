package com.dataox.linkedinscraper.scraping.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Viacheslav Yakovenko
 * @since 11.05.2021
 */
@Data
@Configuration
@ConfigurationProperties("app.rabbitmq")
public class RabbitmqProperties {
    private String defaultReceiveQueue;
}
