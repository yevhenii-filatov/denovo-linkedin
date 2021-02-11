package com.dataox.linkedinscraper;

import com.dataox.ChromeDriverLauncher;
import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * @author Dmitriy Lysko
 * @since 28/01/2021
 */
@Service
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

    private final LinkedinProfileScraper profileScraper;
    private final ChromeOptions chromeOptions;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ChromeDriverLauncher chromeDriverLauncher = new ChromeDriverLauncher(chromeOptions);
        CollectedProfileSourcesDTO scrape;
        try (chromeDriverLauncher) {
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/alexander-demchenko/");
            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/carly-savar-b99b537/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/dmitriy-lysko-607130160/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/duquene-mercier-pierre-108812196/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/pin-chun-liu-021a5695/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/shelly-d-e-collins-3884482b/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/tedros-adhanom-ghebreyesus/");
//            scrape = profileScraper.scrape(chromeDriverLauncher.getWebDriver(), "https://www.linkedin.com/in/linda-noble-85509b10/");
        }
        System.out.println(scrape);
    }
}
