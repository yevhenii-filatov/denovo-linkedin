package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.LinkedinBasicProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedinBasicProfileInfoRepository extends JpaRepository<LinkedinBasicProfileInfo, Long> {
}
