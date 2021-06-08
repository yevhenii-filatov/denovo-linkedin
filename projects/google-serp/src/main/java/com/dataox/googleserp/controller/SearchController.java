package com.dataox.googleserp.controller;

import com.dataox.googleserp.model.dto.InitialDataDTO;
import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.service.InitialDataProcessor;
import com.dataox.googleserp.service.run.SearchStarter;
import com.dataox.notificationservice.service.NotificationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitriy Lysko
 * @since 25/01/2021
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchController {

    private final SearchStarter searchStarter;
    private final InitialDataProcessor initialDataProcessor;
    private final NotificationsService notificationsService;

    @PostMapping
    public ResponseEntity<List<InitialData>> startSearch(@RequestBody @Valid List<InitialDataDTO> initialDataDTOS) {
        Map<InitialData, Integer> dataAndStep = initialDataProcessor.processInitialData(initialDataDTOS);
        List<InitialData> initialData = new ArrayList<>(dataAndStep.keySet());
        searchStarter.startSearch(dataAndStep);
        return ResponseEntity.ok(initialData);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception e) {
        notificationsService.sendAll("GoogleSearch: Received JSON with same denovoId: ".concat(e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Received JSON with same denovoId: ".concat(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        notificationsService.sendAll("GoogleSearch: Failed to start search: ".concat(e.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start search: ".concat(e.getMessage()));
    }
}
