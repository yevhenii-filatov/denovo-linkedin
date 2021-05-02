package com.dataox.loadbalancer.web.controller;

import com.dataox.loadbalancer.domain.dto.LinkedinProfileToUpdateDTO;
import com.dataox.loadbalancer.domain.dto.ScrapingDTO;
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
    public ResponseEntity<String> startInitialScraping(@RequestBody List<ScrapingDTO> scrapingDTOS) {
        scrapingService.startInitialScraping(scrapingDTOS);
        return ResponseEntity.ok("NICE");
    }

    @PostMapping("/rescrape")
    public ResponseEntity<String> rescrapeFixedProfiles(@RequestBody List<Long> notReusableProfileIds) {
        scrapingService.rescrapeFixedProfiles(notReusableProfileIds);
        return ResponseEntity.ok("NICE");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody List<LinkedinProfileToUpdateDTO> profileToUpdateDTOS) {
        scrapingService.updateProfiles(profileToUpdateDTOS);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("NICE");
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + "\nScraping was not started");
    }
}
