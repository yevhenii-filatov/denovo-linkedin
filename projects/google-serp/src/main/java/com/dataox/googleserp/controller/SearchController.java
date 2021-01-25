package com.dataox.googleserp.controller;

import com.dataox.googleserp.service.run.SearchStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        CompletableFuture.runAsync(() -> searchStarter.startSearchWithDenovoIds(denovoIds));
        return ResponseEntity.ok("SUCCESSFULLY STARTED");
    }

    @GetMapping("/start")
    public ResponseEntity<String> startSearch() {
        searchStarter.startSearchForNotSearchedInitialData();
        return ResponseEntity.ok("SUCCESSFULLY STARTED");
    }
}
