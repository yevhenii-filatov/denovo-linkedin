package com.dataox.loadbalancer.web.controller;

import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.dto.InitialDataDTO;
import com.dataox.loadbalancer.service.DataLoaderService;
import com.dataox.loadbalancer.service.GoogleSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 05/04/2021
 */
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class InitialDataController {

    private final DataLoaderService dataLoaderService;
    private final GoogleSearchService googleSearchService;

    @PutMapping
    public ResponseEntity<List<InitialData>> addData(@RequestBody List<InitialDataDTO> initialDataDTOS,
                                                     @RequestParam(required = false, defaultValue = "false") boolean triggerSearch) {
        List<InitialData> initialData = dataLoaderService.saveInitialData(initialDataDTOS);
        if (triggerSearch)
            googleSearchService.triggerGoogleSearch(initialData);
        return ResponseEntity.ok(initialData);
    }
}
