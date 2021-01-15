package com.dataox.scrapingutils.configuration;

import com.dataox.scrapingutils.configuration.properties.ChromedriverProperties;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @author Yevhenii Filatov
 * @since 11/26/20
 */

@Configuration
public class ChromeOptionsConfiguration {
    private final ChromedriverProperties properties;

    public ChromeOptionsConfiguration(ChromedriverProperties properties) {
        this.properties = properties;
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, properties.getPath());
    }

    @Bean
    public ChromeOptions options() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");

        options.setHeadless(properties.getOptions().getHeadless());
        options.addArguments("--log-level=3");
        options.addArguments("--silent");

        options.addArguments("--disable-setuid-sandbox");
        options.addArguments("--disable-infobars");
        options.addArguments("--window-position=0,0");
        options.addArguments("--ignore-certifcate-errors");
        options.addArguments("--ignore-certifcate-errors-spki-list");

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("w3c", false);
        System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");

        return options;
    }
}
