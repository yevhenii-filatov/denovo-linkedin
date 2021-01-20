package com.dataox.googleserp.service.search;

import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchMetadata;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.model.search.SearchQuery;
import com.dataox.googleserp.service.parsing.SearchResultParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Slf4j
@Service
public abstract class AbstractSearchProvider<T> implements SearchProvider<T> {
    @Override
    public Collection<SearchResult> search(SearchQuery query, InitialData initialData) {
        String queryUrl = prepareQueryUrl(query);
        T searchResultsContainer = resolveSearchResultsContainer(queryUrl);
        SearchMetadata metadata = new SearchMetadata();
        metadata.setSearchPageSource(searchResultsContainer.toString());
        metadata.setQueryUrl(queryUrl);
        metadata.setInitialData(initialData);
        initialData.setSearchMetadata(metadata);
        SearchResultParser<T> parser = resolveParser();
        return parser.parse(searchResultsContainer, queryUrl);
    }
}
