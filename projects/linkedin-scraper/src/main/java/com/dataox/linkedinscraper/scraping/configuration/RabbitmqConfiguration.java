package com.dataox.linkedinscraper.scraping.configuration;

import com.dataox.linkedinscraper.scraping.configuration.property.RabbitmqProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Viacheslav Yakovenko
 * @since 11.05.2021
 */
@RequiredArgsConstructor
@Configuration
public class RabbitmqConfiguration {
    private final RabbitmqProperties rabbitmqProperties;
    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void init() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(rabbitmqProperties.getDefaultReceiveQueue(),
                false, false, false, null);
    }

    @PreDestroy
    public void destroy() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
