package com.dataox.googleserp.service.search;

import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.model.search.SearchQuery;
import com.dataox.googleserp.service.parsing.SearchResultParser;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Service
public interface SearchProvider<T> {

    T resolveSearchResultsContainer(String queryUrl);

    SearchResultParser<T> resolveParser();

    String prepareQueryUrl(SearchQuery query);

    Collection<SearchResult> search(SearchQuery query, InitialData initialData);
}
