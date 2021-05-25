package com.dataox.loadbalancer.domain.repositories;

import com.dataox.loadbalancer.domain.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface LinkedinProfileRepository extends JpaRepository<LinkedinProfile, Long> {

}
