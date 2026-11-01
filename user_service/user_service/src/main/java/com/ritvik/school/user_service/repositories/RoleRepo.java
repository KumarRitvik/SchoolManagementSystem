package com.ritvik.school.user_service.repositories;


import com.ritvik.school.user_service.entities.Role;

import com.ritvik.school.user_service.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(RoleName roleName);
}