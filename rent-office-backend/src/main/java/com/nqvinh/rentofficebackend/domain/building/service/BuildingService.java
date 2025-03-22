package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignBuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.response.CustomerResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BuildingService {
    CustomerResDto createBuildingWithCustomer(CustomerReqDto customerReqDto, List<MultipartFile> buildingImages);
    Page<BuildingDto> getCustomerBuildings(Map<String, String> params);
    void deleteBuilding(Long consignmentId);
    BuildingDto updateBuilding(Long buildingId, BuildingDto buildingDto, List<MultipartFile> buildingImages, List<String> deletedImages);
    BuildingDto getBuildingById(Long buildingId);
    void verifyTokenBuilding(String buildingId,String token);
    Page<BuildingDto> getBuildings(Map<String, String> params);
    List<BuildingDto> getAllBuildingCompany();

    //giao toà nhà cho nhân viên quản lý
    void assignmentBuildingToStaffs(AssignBuildingDto assignBuildingDto);
    List<UserDto> getStaffsByBuildingId(Long buildingId);
}
