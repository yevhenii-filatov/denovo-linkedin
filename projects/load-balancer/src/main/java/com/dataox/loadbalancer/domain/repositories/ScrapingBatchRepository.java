package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.ScrapingBatch;
import com.dataox.loadbalancer.domain.entities.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
public interface ScrapingBatchRepository extends JpaRepository<ScrapingBatch, Long> {

    ScrapingBatch findBySearchResultsIn(List<SearchResult> searchResults);
}
