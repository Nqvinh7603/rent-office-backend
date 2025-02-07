package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;

import java.util.List;
import java.util.Map;

public interface BuildingLevelService {
    BuildingLevelDto createBuildingLevel(BuildingLevelDto buildingLevelDto);

    BuildingLevelDto updateBuildingLevel(Long buildingLevelId, BuildingLevelDto buildingLevelDto);

    void deleteBuildingLevel(Long id);

    List<BuildingLevelDto> getAllBuildingLevels();

    Page<BuildingLevelDto> getBuildingLevels(Map<String, String> params);

}
