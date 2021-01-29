package com.dataox.linkedinscraper;

import com.dataox.linkedinscraper.dto.CollectedProfileSourcesDTO;
import com.dataox.linkedinscraper.scraping.scrapers.LinkedinProfileScraper;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CollectedProfileSourcesDTO scrape = profileScraper.scrape("https://www.linkedin.com/in/carly-savar-b99b537/");
        System.out.println(scrape);
    }
}
