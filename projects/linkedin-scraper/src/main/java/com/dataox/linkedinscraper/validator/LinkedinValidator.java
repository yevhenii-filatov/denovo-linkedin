package com.dataox.linkedinscraper.validator;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.dto.LinkedinProfileToScrapeDTO;
import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import com.dataox.linkedinscraper.parser.LinkedinProfileParser;
import com.dataox.linkedinscraper.parser.dto.LinkedinProfile;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import com.dataox.linkedinscraper.scraping.service.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
@RequiredArgsConstructor
public class LinkedinValidator {

    private final LinkedinProfileScraper scraper;
    private final LinkedinProfileParser parser;
    private final LoginService loginService;
    private final ChromeOptions chromeOptions;
    private final ScraperValidator scraperValidator;
    private final ParserValidator parserValidator;

    public List<ValidationField> validate() throws LinkedinValidatorException {
        List<ValidationField> validationResult = new ArrayList<>();
        LinkedinProfileToScrapeDTO profileToScrape = getProfileToScrape();

        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--remote-debugging-port=9222");

        try (ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions)) {
            WebDriver webDriver = launcher.getWebDriver();
            loginService.performLogin(webDriver);

            CollectedProfileSourcesDTO scrapedProfile = scraper.scrape(webDriver, profileToScrape);

            validationResult.addAll(scraperValidator.checkScraper(scrapedProfile));

            LinkedinProfile parsedProfile = parser.parse(scrapedProfile);

            validationResult.addAll(parserValidator.checkParser(parsedProfile));

            return validationResult
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    private LinkedinProfileToScrapeDTO getProfileToScrape() {
        LinkedinProfileToScrapeDTO profileToScrape = new LinkedinProfileToScrapeDTO();
        profileToScrape.setProfileURL("https://www.linkedin.com/in/john-tester-603570213/");
        profileToScrape.setOptionalFieldsContainer(getProfileOptionalFieldsContainer());
        profileToScrape.setSearchResultId(0L);
        return profileToScrape;
    }

    private OptionalFieldsContainer getProfileOptionalFieldsContainer() {
        OptionalFieldsContainer optionalFieldsContainer = new OptionalFieldsContainer();
        optionalFieldsContainer.setScrapeAccomplishments(true);
        optionalFieldsContainer.setScrapeActivities(true);
        optionalFieldsContainer.setScrapeInterests(true);
        optionalFieldsContainer.setScrapeLicenses(true);
        optionalFieldsContainer.setScrapeSkills(true);
        optionalFieldsContainer.setScrapeVolunteer(true);
        optionalFieldsContainer.setScrapeRecommendations(true);
        return optionalFieldsContainer;
    }
}
