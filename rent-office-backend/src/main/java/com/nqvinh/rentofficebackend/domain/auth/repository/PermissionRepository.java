package com.nqvinh.rentofficebackend.domain.auth.repository;

import com.nqvinh.rentofficebackend.domain.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
