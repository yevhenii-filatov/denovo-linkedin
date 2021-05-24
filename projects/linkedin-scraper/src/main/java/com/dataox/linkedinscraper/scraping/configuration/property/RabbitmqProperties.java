package com.dataox.linkedinscraper.scraping.configuration.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Viacheslav Yakovenko
 * @since 11.05.2021
 */
@Data
@Configuration
@ConfigurationProperties("spring.rabbitmq")
public class RabbitmqProperties {
    private String host;
    private String port;
    private String username;
    private String password;
    private Template template;  //outer class for defaultReceiveQueue

    public String getDefaultReceiveQueue() {
        return template.getDefaultReceiveQueue();
    }

    public void setDefaultReceiveQueue(String defaultReceiveQueue) {
        template.setDefaultReceiveQueue(defaultReceiveQueue);
    }

    @Getter
    @Setter
    @ConfigurationProperties("spring.rabbitmq.template")
    private static class Template {
        private String defaultReceiveQueue;
    }
}
