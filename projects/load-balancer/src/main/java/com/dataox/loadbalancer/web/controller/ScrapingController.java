package com.dataox.loadbalancer.web.controller;

import com.dataox.loadbalancer.exception.DataNotFoundException;
import com.dataox.loadbalancer.service.ScrapingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@RestController
@RequestMapping("/scraping")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScrapingController {
    ScrapingService scrapingService;

    @PostMapping("/initial")
    public ResponseEntity<String> startInitialScraping(@RequestBody List<Long> denovoIds) {
        scrapingService.startInitialScraping(denovoIds);
        return ResponseEntity.ok("NICE");
    }

    @PostMapping("/rescrape")
    public ResponseEntity<String> rescrapeFixedProfiles(@RequestBody List<Long> notReusableProfileIds) {
        scrapingService.rescrapeFixedProfiles(notReusableProfileIds);
        return ResponseEntity.ok("NICE");
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + "\nScraping not started");
    }
}
