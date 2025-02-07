package com.nqvinh.rentofficebackend.application.api.building;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.BUILDINGS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingController {
    BuildingService buildingService;

    @PostMapping
    public ApiResponse<BuildingDto> createBuilding(@Valid @RequestPart("building") BuildingDto buildingDto, @RequestParam(value = "building_img", required = false) List<MultipartFile> buildingImg) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Building"))
                .payload(buildingService.createBuilding(buildingDto, buildingImg))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_BUILDING)
    public ApiResponse<BuildingDto> updateBuilding(@PathVariable Long id, @Valid @RequestPart("building") BuildingDto buildingDto, @RequestParam(value = "building_img", required = false) List<MultipartFile> buildingImg) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Building"))
                .payload(buildingService.updateBuilding(id, buildingDto, buildingImg))
                .build();
    }


    @DeleteMapping(UrlConstant.DELETE_BUILDING)
    public ApiResponse<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Building"))
                .build();
    }


    @GetMapping
    public ApiResponse<Page<BuildingDto>> getBuildings(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Buildings"))
                .payload(buildingService.getBuildings(params))
                .build();
    }

    @GetMapping(UrlConstant.GET_BUILDING_BY_ID)
    public ApiResponse<BuildingDto> getBuildingById(@PathVariable Long id) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building"))
                .payload(buildingService.getBuildingById(id))
                .build();
    }


}
