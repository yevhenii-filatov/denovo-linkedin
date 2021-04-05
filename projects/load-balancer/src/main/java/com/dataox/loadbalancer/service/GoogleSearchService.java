package com.dataox.loadbalancer.service;

import com.dataox.loadbalancer.configuration.property.GoogleSearchProperties;
import com.dataox.loadbalancer.domain.entities.InitialData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dmitriy Lysko
 * @since 05/04/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSearchService {
    private final RestTemplate restTemplate;
    private final GoogleSearchProperties searchProperties;

    public void triggerGoogleSearch(List<InitialData> initialDataList) {
        List<Long> denovoIdsToSearch = initialDataList.stream()
                .map(InitialData::getDenovoId)
                .collect(Collectors.toList());
        log.info("Triggering google search for denovo ids {}", denovoIdsToSearch);
        String response = restTemplate.postForObject(searchProperties.getServiceUrl(), denovoIdsToSearch, String.class);
        log.info("Response from google search: {}", response);
    }
}
