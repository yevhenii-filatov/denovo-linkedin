package com.dataox.linkedinscraper;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 24/02/2021
 */
@Service
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {
    private final LoginService loginService;
    private final LinkedinProfileScraper scraper;
    private final LinkedinProfileParser parser;
    private final ChromeOptions chromeOptions;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> profileUrl = Arrays.asList(
//                "https://www.linkedin.com/in/daymesanchez/",
//                "https://www.linkedin.com/in/ashley-scheer-3b4155b/",
//                "https://www.linkedin.com/in/lisa-umans-18350465/",
//                "https://www.linkedin.com/in/winniedotwong/",
//                "https://www.linkedin.com/in/evelyn-sahr-2615b212/",
//                "https://www.linkedin.com/in/michael-mason-5196898/",
//                "https://www.linkedin.com/in/clairebendix/",
//                "https://www.linkedin.com/in/andykgordon/",
//                "https://www.linkedin.com/in/markostrau/",
                "https://www.linkedin.com/in/davidjohnball/"
        );
        List<CollectedProfileSourcesDTO> sourcesDTOS = new ArrayList<>();
        ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions);
        try (launcher) {
            WebDriver webDriver = launcher.getWebDriver();
            loginService.performLogin(webDriver);
            for (String profileURL : profileUrl) {
                sourcesDTOS.add(scraper.scrape(webDriver, profileURL));
            }
        }
        List<LinkedinProfile> profiles = new ArrayList<>();
        sourcesDTOS.stream()
                .map(parser::parse)
                .forEach(profiles::add);
        List<String> profileJsons = new ArrayList<>();
        for (LinkedinProfile profile : profiles) {
            profileJsons.add(objectMapper.writeValueAsString(profile));
        }
        System.out.println(profileJsons);
    }
}
