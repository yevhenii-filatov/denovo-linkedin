package com.dataox.googleserp.service.sending;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 25/01/2021
 */
@Service
public class SearchResultSender {

    public void sendSearchResultIdsToLoadBalancer(List<Long> searchResultIds) {
        System.out.println(searchResultIds);
    }
}
