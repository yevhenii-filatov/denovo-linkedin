package com.dataox.googleserp.repository;

import com.dataox.googleserp.model.entity.InitialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Repository
public interface InitialDataRepository extends JpaRepository<InitialData, Long> {
    List<InitialData> findAllBySearchedFalse();

    List<InitialData> findAllByIdInAndSearchedFalse(List<Long> id);

    List<InitialData> findAllByIdBetween(Long start, Long end);

    List<InitialData> findAllByDenovoIdInAndSearchedFalse(List<Long> id);

    Optional<InitialData> findByIdAndSearchedFalse(Long id);
}
