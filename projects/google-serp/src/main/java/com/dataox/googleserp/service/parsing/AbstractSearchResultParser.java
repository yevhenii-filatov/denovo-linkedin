package com.dataox.googleserp.service.parsing;

import com.dataox.googleserp.model.entity.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Slf4j
@Service
public abstract class AbstractSearchResultParser<T> implements SearchResultParser<T> {

    @Override
    public Collection<SearchResult> parse(T container, String queryUrl, int searchStep) {
        if (Objects.isNull(container)) {
            log.warn("EMPTY SOURCES FOR QUERY: {}", queryUrl);
            return null;
        }
        Collection<T> searchResultsElements = splitResults(container);
        if (searchResultsElements.isEmpty()) {
            log.info("NO SEARCH RESULTS FOR QUERY {}", queryUrl);
            return Collections.emptyList();
        }
        AtomicInteger position = new AtomicInteger(1);
        return searchResultsElements.stream()
                .map(this::parseOne)
                .filter(searchResult -> searchResult.getUrl().matches("https://www\\.linkedin\\.com/in/(\\w+-?)+(\\d+)?/?"))
                .peek(searchResult -> searchResult.setSearchPosition(position.getAndIncrement()))
                .peek(searchResult -> searchResult.setSearchStep(searchStep))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public SearchResult parseOne(T element) {
        return SearchResult.builder()
                .collectedAt(Instant.now())
                .title(fetchTitle(element))
                .url(fetchUrl(element))
                .description(fetchDescription(element))
                .build();
    }
}
