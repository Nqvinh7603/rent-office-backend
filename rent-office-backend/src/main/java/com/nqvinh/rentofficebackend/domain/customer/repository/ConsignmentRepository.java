package com.nqvinh.rentofficebackend.domain.customer.repository;

import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
}
