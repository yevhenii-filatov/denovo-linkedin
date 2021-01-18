package com.dataox.googleserp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {
		"com.dataoxx.scrapingutils",
		"com.dataox.googleserp"
})
@ConfigurationPropertiesScan("com.dataox.googleserp.configuration")
public class GoogleSerpApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleSerpApplication.class, args);
	}

}
