package com.dataox.googleserp.service.sending;

import com.dataox.googleserp.configuration.properties.ServiceProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 25/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchResultSender {
    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    public void sendSearchResultIdsToLoadBalancer(List<Long> searchResultIds) {
        log.info("Sending search result ids to load balancer: {}",searchResultIds);
        String response = restTemplate.postForObject(serviceProperties.getLoadBalancerUrl(), searchResultIds, String.class);
        log.info("Response from load balancer: {}", response);
    }
}
