package com.dataox.linkedinscraper.service.validator;

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

/**
 * @author Mykola Kostyshyn
 * @since 27/05/2021
 */
@Service
@RequiredArgsConstructor
public class LinkedinValidator{

    private final LinkedinProfileScraper scraper;
    private final LinkedinProfileParser parser;
    private final LoginService loginService;
    private final ChromeOptions chromeOptions;
    private final ScraperValidator scraperValidator;
    private final ParserValidator parserValidator;

    public void validate() throws LinkedinValidatorException {
        LinkedinProfileToScrapeDTO profileToScrape = getProfileToScrape();

        try (ChromeDriverLauncher launcher = new ChromeDriverLauncher(chromeOptions)) {
            WebDriver webDriver = launcher.getWebDriver();
            loginService.performLogin(webDriver);

            CollectedProfileSourcesDTO scrapedProfile = scraper.scrape(webDriver, profileToScrape);

            scraperValidator.checkScraper(scrapedProfile);

            LinkedinProfile parsedProfile = parser.parse(scrapedProfile);

            parserValidator.checkParser(parsedProfile);
        }
    }

    private LinkedinProfileToScrapeDTO getProfileToScrape() {
        LinkedinProfileToScrapeDTO profileToScrape = new LinkedinProfileToScrapeDTO();
        profileToScrape.setProfileURL("URL");
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
