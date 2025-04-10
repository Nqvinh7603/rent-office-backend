package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BuildingImageService {
    void updateBuildingImages(Building building, List<MultipartFile> buildingImages);
    void deleteBuildingImages(Building building, List<String> deletedImages);
    List<Building> convertBuildingDtoToEntities(CustomerReqDto customerReqDto, List<String> uploadedUrls, Customer customer);
    List<String> uploadBuildingImages(CustomerReqDto customerReqDto, List<MultipartFile> buildingImages);
}
