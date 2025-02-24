package com.nqvinh.rentofficebackend.domain.auth.repository;

import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
}