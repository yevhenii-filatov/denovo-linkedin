package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.InitialData;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    List<SearchResult> findAllByInitialDataRecordIn(List<InitialData> initialData);

    List<SearchResult> findAllByUrlIn(List<String> profileUrls);

    SearchResult findByUrl(String profileUrl);

    List<SearchResult> findAllByIdIn(List<Long> searchResultIdsIn);
}
