package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.constant.BuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingLevelRepository extends JpaRepository<BuildingLevel, Long>, JpaSpecificationExecutor<BuildingLevel> {

   @Query("SELECT bl FROM BuildingLevel bl JOIN bl.buildings b WHERE b.buildingStatus = :buildingStatus AND b.city LIKE %:city%")
   List<BuildingLevel> findByBuildingStatusAndCity(BuildingStatus buildingStatus, String city);
}
