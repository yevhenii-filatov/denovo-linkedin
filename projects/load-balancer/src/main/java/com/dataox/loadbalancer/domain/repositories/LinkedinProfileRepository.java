package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.LinkedinProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedinProfileRepository extends JpaRepository<LinkedinProfile, Long> {

}
