package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BuildingService {
    BuildingDto createBuilding(BuildingDto buildingDto, List<MultipartFile> buildingImages);

    BuildingDto updateBuilding(Long buildingId, BuildingDto buildingDto, List<MultipartFile> buildingImages);

    BuildingDto getBuildingById(Long buildingId);

    void deleteBuilding(Long buildingId);

    Page<BuildingDto> getBuildings(Map<String, String> params);
}
