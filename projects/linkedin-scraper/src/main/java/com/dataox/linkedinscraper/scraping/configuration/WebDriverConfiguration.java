package com.dataox.linkedinscraper.scraping.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Configuration
public class WebDriverConfiguration {

    @Bean
    public WebDriver webDriver() {
        WebDriver webDriver = new ChromeDriver(createChromeOptions());
        webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        return webDriver;
    }

    public ChromeOptions createChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--whitelisted-ips=''");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--proxy-server=http://5.189.151.227:24079");
//        chromeOptions.addArguments("--proxy-server=http://34.122.76.79:8888");
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        return chromeOptions;
    }
}
