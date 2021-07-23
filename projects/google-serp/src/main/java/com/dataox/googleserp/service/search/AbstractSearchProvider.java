package com.dataox.googleserp.service.search;

import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchMetadata;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.model.search.SearchQuery;
import com.dataox.googleserp.service.parsing.SearchResultParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

import static com.dataox.googleserp.util.GoogleSearchParameters.prepareParameter;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Yevhenii Filatov
 * @since 12/25/20
 */

@Slf4j
@Service
public abstract class AbstractSearchProvider<T> implements SearchProvider<T> {

    private static final int SEARCH_STEPS_AMOUNT = 7;

    @Override
    public Collection<SearchResult> search(InitialData initialData, int searchStep) {

        if (1 == searchStep && isNotBlank(initialData.getLinkedinUrl())) {
            return Collections.singletonList(createSingleSearchResult(initialData.getLinkedinUrl()));
        }
        Collection<SearchResult> searchResults = emptyList();
        SearchQuery searchQuery;
        T searchResultsContainer;
        SearchResultParser<T> parser = resolveParser();
        if (searchStep == 1) {
            searchStep++;
        }

        for (; searchStep <= SEARCH_STEPS_AMOUNT; searchStep++) {
            searchQuery = new SearchQueryProvider(initialData).getQuery(searchStep);
            String queryUrl = prepareQueryUrl(searchQuery);

            searchResultsContainer = resolveSearchResultsContainer(queryUrl);
            SearchMetadata metadata = createMetaData(initialData, queryUrl, searchResultsContainer);
            initialData.setSearchMetadata(metadata);

            searchResults = parser.parse(searchResultsContainer, queryUrl, searchStep);
            if (nonNull(searchResults) && !searchResults.isEmpty())
                return searchResults;
        }
        return searchResults;
    }

    private SearchMetadata createMetaData(InitialData initialData, String queryUrl, T searchResultsContainer) {
        SearchMetadata metadata = new SearchMetadata();
        metadata.setSearchPageSource(searchResultsContainer.toString());
        metadata.setQueryUrl(queryUrl);
        metadata.setInitialData(initialData);
        return metadata;
    }

    private SearchResult createSingleSearchResult(String profileUrl) {
        SearchResult searchResult = new SearchResult();
        searchResult.setUrl(profileUrl);
        searchResult.setSearchStep(1);
        searchResult.setSearchPosition(1);
        return searchResult;
    }

    @AllArgsConstructor
    static class SearchQueryProvider {
        InitialData initialData;

        public SearchQuery getQuery(int step) {
            String firstName = initialData.getFirstName();
            String lastName = initialData.getLastName();
            String middleName = initialData.getMiddleName();
            String firmName = initialData.getFirmName();
            String firstLastNames = String.format("%s %s", firstName, lastName);
            String firstAndMiddleNames = String.format("%s %s", firstName, middleName);
            switch (step) {
                case 2:
                    return buildQuery(firstLastNames, firmName);
                case 3:
                    return buildQuery(lastName, firmName);
                case 4:
                    return buildQuery(firstAndMiddleNames, firmName);
                case 5:
                    return buildQuery(firstLastNames, EMPTY);
                case 6:
                    return buildQuery(lastName, EMPTY);
                case 7:
                    return buildQuery(firstAndMiddleNames, EMPTY);
                default:
                    throw new IllegalArgumentException("Step must be >=2 & <=7");
            }
        }

        private SearchQuery buildQuery(String inTitle, String inText) {
            return SearchQuery.fromQueryParameters(
                    prepareParameter("site", "linkedin.com/in"),
                    prepareParameter("intitle", inTitle),
                    prepareParameter("intext", inText)
            );
        }
    }

}
