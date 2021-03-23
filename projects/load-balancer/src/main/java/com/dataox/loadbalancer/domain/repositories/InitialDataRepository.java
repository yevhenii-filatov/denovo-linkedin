package com.dataox.loadbalancer.domain.repositories;


import com.dataox.loadbalancer.domain.entities.InitialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InitialDataRepository extends JpaRepository<InitialData, Long> {
    List<InitialData> findAllBySearchedFalse();

    List<InitialData> findAllByIdInAndSearchedFalse(List<Long> id);

    List<InitialData> findAllByIdBetween(Long start, Long end);

    Optional<InitialData> findByIdAndSearchedFalse(Long id);

    List<InitialData> findAllByDenovoIdIn(List<Long> denovoIds);

}
