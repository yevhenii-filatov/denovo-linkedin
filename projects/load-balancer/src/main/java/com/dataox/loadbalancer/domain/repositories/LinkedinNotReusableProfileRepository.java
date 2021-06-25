package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.LinkedinNotReusableProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dmitriy Lysko
 * @since 23/03/2021
 */
public interface LinkedinNotReusableProfileRepository extends JpaRepository<LinkedinNotReusableProfile, Long> {
}
