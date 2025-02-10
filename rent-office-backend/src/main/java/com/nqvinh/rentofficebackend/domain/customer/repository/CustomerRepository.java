package com.nqvinh.rentofficebackend.domain.customer.repository;

import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}
