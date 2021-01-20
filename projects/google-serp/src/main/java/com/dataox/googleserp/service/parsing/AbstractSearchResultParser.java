package com.dataox.googleserp.service.parsing;

import com.dataox.googleserp.model.entity.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Slf4j
@Service
public abstract class AbstractSearchResultParser<T> implements SearchResultParser<T> {

    @Override
    public Collection<SearchResult> parse(T container, String queryUrl) {
        if (Objects.isNull(container)) {
            log.warn("EMPTY SOURCES FOR QUERY: {}", queryUrl);
            return null;
        }
        Collection<T> searchResultsElements = splitResults(container);
        if (searchResultsElements.isEmpty()) {
            log.info("NO SEARCH RESULTS FOR QUERY {}", queryUrl);
            return Collections.emptyList();
        }
        return searchResultsElements.stream()
                .map(this::parseOne)
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
