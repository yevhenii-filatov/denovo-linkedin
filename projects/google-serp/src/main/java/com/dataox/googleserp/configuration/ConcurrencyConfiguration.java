package com.dataox.googleserp.configuration;

import com.dataox.googleserp.configuration.properties.SearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ConcurrencyConfiguration {

    @Bean
    public ExecutorService executorService(SearchProperties searchProperties) {
        return Executors.newFixedThreadPool(searchProperties.getConcurrencyRestriction());
    }
}
