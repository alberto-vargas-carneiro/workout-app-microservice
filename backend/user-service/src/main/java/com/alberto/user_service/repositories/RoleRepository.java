package com.alberto.user_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alberto.user_service.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
