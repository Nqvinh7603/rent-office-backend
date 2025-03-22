package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findByRequireType(RequireTypeEnum requireType);
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.users u WHERE c.requireType = :requireType AND u.userId = :userId")
    List<Customer> findCustomersByRequireTypeAndUser(@Param("requireType") RequireTypeEnum requireType, @Param("userId") UUID userId);

    @Query("SELECT c FROM Customer c WHERE c.status IS NOT NULL")
    Page<Customer> findCustomersWithStatusNotNull(Specification<Customer> spec, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.status IS NOT NULL")
    List<Customer> findPotentialCustomer();
}
