package com.dataox.loadbalancer.web.controller;

import com.dataox.linkedinscraper.dto.ScrapingResultsDTO;
import com.dataox.loadbalancer.service.ScrapingService;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/scraping/receive")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServicesController {
    ScrapingService scrapingService;
    NotificationsService notificationsService;

    @PostMapping("/scraped")
    public ResponseEntity<String> receiveScrapedProfiles(@RequestBody @Valid ScrapingResultsDTO scrapingResultsDTO) {
        scrapingService.processScrapingResults(scrapingResultsDTO);
        return ResponseEntity.ok("Fine");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error(                        "LoadBalancer: Exception appeared during POST query to /scraping/receive/scraped. Exception message: ".concat(e.getMessage()));
        notificationsService.sendInternal("LoadBalancer: Exception appeared during POST query to /scraping/receive/scraped. Exception message: ".concat(e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
