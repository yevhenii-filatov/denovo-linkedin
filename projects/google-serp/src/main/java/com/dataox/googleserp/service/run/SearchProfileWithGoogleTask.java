package com.dataox.googleserp.service.run;

import com.dataox.googleserp.model.entity.InitialData;
import com.dataox.googleserp.model.entity.SearchResult;
import com.dataox.googleserp.repository.InitialDataRepository;
import com.dataox.googleserp.service.search.GoogleSearchProvider;
import com.dataox.googleserp.util.BeanUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Yevhenii Filatov
 * @since 12/24/20
 */
@Slf4j
@RequiredArgsConstructor
public class SearchProfileWithGoogleTask implements Runnable {
    private final InitialData initialData;
    private final int searchStep;

    @Override
    @Transactional
    public void run() {
        GoogleSearchProvider searchProvider = BeanUtils.getBean(GoogleSearchProvider.class);
        InitialDataRepository initialDataRepository = BeanUtils.getBean(InitialDataRepository.class);
        Collection<SearchResult> results = searchProvider.search(initialData, searchStep);
        if (Objects.isNull(results)) {
            return;
        }
        results.forEach(searchResult -> searchResult.setInitialDataRecord(initialData));
        initialData.setNoResults(results.isEmpty());
        initialData.setSearched(true);
//        initialData.getSearchResults().clear(); //uncomment when pinchun finish playing with results
        initialData.getSearchResults().addAll(results);
        initialData.setUpdatedAt(Instant.now());
        initialDataRepository.save(initialData);
    }
}