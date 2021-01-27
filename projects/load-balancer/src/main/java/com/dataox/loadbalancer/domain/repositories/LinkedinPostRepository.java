package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.LinkedinPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedinPostRepository extends JpaRepository<LinkedinPost, Long> {
}
