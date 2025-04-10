package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;

import java.util.List;
import java.util.Map;

public interface BuildingTypeService {
    BuildingTypeDto createBuildingType(BuildingTypeDto buildingTypeDto);

    BuildingTypeDto updateBuildingType(Long buildingTypeId, BuildingTypeDto buildingTypeDto);

    void deleteBuildingType(Long id);

    Page<BuildingTypeDto> getBuildingTypes(Map<String, String> params);

    List<BuildingTypeDto> getAllBuildingTypes();
}
