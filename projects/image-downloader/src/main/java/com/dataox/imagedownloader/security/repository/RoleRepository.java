package com.dataox.imagedownloader.security.repository;

import com.dataox.imagedownloader.security.models.ERole;
import com.dataox.imagedownloader.security.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
