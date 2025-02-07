package com.nqvinh.rentofficebackend.domain.auth.repository;

import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleName(String name);

    boolean existsRoleByRoleId(Long id);
}