package com.dataox.loadbalancer.domain.repositories;


import com.dataox.loadbalancer.domain.entities.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
}
