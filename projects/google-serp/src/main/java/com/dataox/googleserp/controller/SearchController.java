package com.dataox.googleserp.controller;

import com.dataox.googleserp.exceptions.SearchException;
import com.dataox.googleserp.service.run.SearchStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 25/01/2021
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchController {

    private final SearchStarter searchStarter;

    @PostMapping("/start")
    public ResponseEntity<String> startSearch(@RequestBody List<Long> denovoIds) {
        searchStarter.startSearchWithDenovoIds(denovoIds);
        return ResponseEntity.ok("SUCCESSFULLY STARTED");
    }

    @GetMapping("/start")
    public ResponseEntity<String> startSearch() {
        searchStarter.startSearchForNotSearchedInitialData();
        return ResponseEntity.ok("SUCCESSFULLY STARTED");
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<String> handleStartSearchException(SearchException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start search: ".concat(e.getMessage()));
    }
}
