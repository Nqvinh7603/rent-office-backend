package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BuildingImageService {
    void updateBuildingImages(Building building, List<MultipartFile> buildingImages);
}
