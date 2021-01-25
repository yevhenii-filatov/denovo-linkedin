package com.dataox.googleserp.repository;

import com.dataox.googleserp.model.entity.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {

    List<SearchResult> findAllByInitialDataRecordIdIn(List<Long> initialDataRecordIds);
}
