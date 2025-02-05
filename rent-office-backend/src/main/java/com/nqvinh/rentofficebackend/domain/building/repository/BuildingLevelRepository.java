package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingLevelRepository extends JpaRepository<BuildingLevel, Long>, JpaSpecificationExecutor<BuildingLevel> {
}
