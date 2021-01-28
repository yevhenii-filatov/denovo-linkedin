package com.dataox.linkedinscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.dataox"
})
public class LinkedinScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkedinScraperApplication.class, args);
    }

}
