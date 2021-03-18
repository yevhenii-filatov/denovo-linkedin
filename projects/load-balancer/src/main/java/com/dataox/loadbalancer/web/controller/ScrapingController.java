package com.dataox.loadbalancer.web.controller;

import com.dataox.loadbalancer.service.ScrapingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
