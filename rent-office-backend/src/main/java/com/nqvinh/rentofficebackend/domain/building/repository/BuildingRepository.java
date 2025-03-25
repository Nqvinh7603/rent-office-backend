package com.nqvinh.rentofficebackend.domain.building.repository;

import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Long>, JpaSpecificationExecutor<Building> {
    List<Building> findAllByBuildingStatusIsNotNull();
    @Query("SELECT DISTINCT b.street FROM Building b WHERE b.ward LIKE %:ward% AND b.district LIKE %:district%")
    List<String> findDistinctStreetsByWardAndDistrict(String ward, String district);

}
