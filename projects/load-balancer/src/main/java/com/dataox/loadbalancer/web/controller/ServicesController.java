package com.dataox.loadbalancer.web.controller;

import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.loadbalancer.service.DataLoaderService;
import com.dataox.loadbalancer.service.ScrapingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@RestController
@RequestMapping("/scraping")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ServicesController {
    ScrapingService scrapingService;
    DataLoaderService dataLoaderService;

    @PostMapping("/receive/scraped")
    public ResponseEntity<String> receiveScrapedProfiles(@RequestBody ScrapingResultsDTO scrapingResultsDTO) {
        dataLoaderService.saveLinkedinProfiles(scrapingResultsDTO.getSuccessfulProfiles());
        return ResponseEntity.ok("Fine");
    }
}
