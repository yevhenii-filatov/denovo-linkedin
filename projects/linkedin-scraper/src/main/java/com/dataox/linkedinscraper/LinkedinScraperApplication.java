package com.dataox.linkedinscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {
        "com.dataox"
})
public class LinkedinScraperApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LinkedinScraperApplication.class, args);
    }
}
