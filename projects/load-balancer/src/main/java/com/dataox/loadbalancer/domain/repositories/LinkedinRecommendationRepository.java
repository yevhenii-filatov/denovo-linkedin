package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.LinkedinRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedinRecommendationRepository extends JpaRepository<LinkedinRecommendation, Long> {
}
