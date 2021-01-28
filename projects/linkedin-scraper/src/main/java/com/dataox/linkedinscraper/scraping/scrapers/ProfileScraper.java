package com.dataox.linkedinscraper.scraping.scrapers;

import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Service
@RequiredArgsConstructor
public class ProfileScraper {

    private static final String LINKEDIN_LOGIN_PAGE_URL = "https://www.linkedin.com/";
    private final WebDriver webDriver;
    private final LoginService loginService;


    public void scrape() {
        webDriver.get(LINKEDIN_LOGIN_PAGE_URL);
        loginService.performLogin(webDriver);

    }
}
