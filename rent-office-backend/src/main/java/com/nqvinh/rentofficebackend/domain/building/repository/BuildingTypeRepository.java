package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingTypeRepository extends JpaRepository<BuildingType, Long> {
}
