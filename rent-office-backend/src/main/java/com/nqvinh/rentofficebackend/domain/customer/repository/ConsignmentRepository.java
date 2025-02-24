package com.nqvinh.rentofficebackend.domain.customer.repository;

import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long>, JpaSpecificationExecutor<Consignment> {
}
