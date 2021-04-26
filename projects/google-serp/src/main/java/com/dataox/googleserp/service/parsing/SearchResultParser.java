package com.dataox.googleserp.service.parsing;

import com.dataox.googleserp.model.entity.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Service
public interface SearchResultParser<T> {
    Collection<SearchResult> parse(T container, String queryUrl, int searchStep);

    SearchResult parseOne(T element);

    Collection<T> splitResults(T container);

    String fetchUrl(T element);

    String fetchTitle(T element);

    String fetchDescription(T element);
}
