package com.nqvinh.rentofficebackend.domain.auth.repository;

import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmail(String email);

    List<User> findByUserIdIn(List<UUID> userIds);

    List<User> findByActiveAndRole_RoleName(boolean status, String roleName);

    List<User> findByActiveAndRole_RoleNameIn(boolean status, List<String> roleNames);

    @Query("SELECT u FROM User u JOIN u.customers c WHERE (c.customerId = :customerId AND u.active = true) OR u.role.roleName = 'ADMIN'")
    List<User> findUsersByCustomerId(@Param("customerId") Long customerId);
}