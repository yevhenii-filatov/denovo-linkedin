package com.dataox.loadbalancer.web.controller;

import com.dataox.loadbalancer.dto.LinkedinProfileToUpdateDTO;
import com.dataox.loadbalancer.exception.DataNotFoundException;
import com.dataox.loadbalancer.service.ScrapingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Validated
@RestController
@RequestMapping("/scraping")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScrapingController {
    ScrapingService scrapingService;

    @PostMapping("/rescrape")
    public ResponseEntity<String> rescrapeFixedProfiles(@RequestBody List<Long> notReusableProfileIds) {
        scrapingService.rescrapeFixedProfiles(notReusableProfileIds);
        return ResponseEntity.ok("NICE");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody List<LinkedinProfileToUpdateDTO> profileToUpdateDTOS) {
        scrapingService.updateProfiles(profileToUpdateDTOS);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("NICE");
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + "\nScraping was not started");
    }
}
