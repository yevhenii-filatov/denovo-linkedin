package com.dataox.googleserp.service.run;

import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.model.search.SearchQuery;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.service.search.GoogleSearchProvider;
import com.dataox.googleserp.util.BeanUtils;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import static com.dataox.googleserp.util.GoogleSearchParameters.prepareParameter;


/**
 * @author Yevhenii Filatov
 * @since 12/24/20
 */

@RequiredArgsConstructor
public class SearchProfileWithGoogleTask implements Runnable {
    private final InitialData initialData;

    @Override
    @Transactional
    public void run() {
        GoogleSearchProvider searchProvider = BeanUtils.getBean(GoogleSearchProvider.class);
        InitialDataRepository initialDataRepository = BeanUtils.getBean(InitialDataRepository.class);
        SearchQuery query = prepareQuery();
        Collection<SearchResult> results = searchProvider.search(query, initialData);
        if (Objects.isNull(results)) {
            return;
        }
        results.forEach(searchResult -> searchResult.setInitialDataRecord(initialData));
        initialData.setNoResults(results.isEmpty());
        initialData.setSearched(true);
        initialData.getSearchResults().clear();
        initialData.getSearchResults().addAll(results);
        initialData.setUpdatedAt(Instant.now());
        initialDataRepository.save(initialData);
        System.out.println();
    }

    private SearchQuery prepareQuery() {
        String firmName = initialData.getFirmName();
        return SearchQuery.fromQueryParameters(
                prepareParameter("site", "linkedin.com/in"),
                prepareParameter("intitle", initialData.getLastName()),
                prepareParameter("intext", firmName)
        );
    }
}