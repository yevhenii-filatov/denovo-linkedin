package com.dataox.googleserp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {
        "com.dataox"
})
public class GoogleSerpApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleSerpApplication.class, args);
    }

}

