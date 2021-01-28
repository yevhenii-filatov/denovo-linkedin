package com.dataox.linkedinscraper;

import com.dataox.linkedinscraper.scraping.scrapers.ProfileScraper;
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

    private final ProfileScraper profileScraper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        profileScraper.scrape();
    }
}
