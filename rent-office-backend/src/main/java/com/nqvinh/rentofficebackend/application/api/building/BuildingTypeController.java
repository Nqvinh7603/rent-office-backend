package com.nqvinh.rentofficebackend.application.api.building;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingTypeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.BUILDING_TYPES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingTypeController {

    BuildingTypeService buildingTypeService;

    @PostMapping
    public ApiResponse<BuildingTypeDto> createBuildingType(@RequestBody @Valid BuildingTypeDto buildingTypeDto) {
        return ApiResponse.<BuildingTypeDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Building type"))
                .payload(buildingTypeService.createBuildingType(buildingTypeDto))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_BUILDING_TYPE)
    public ApiResponse<BuildingTypeDto> updateBuildingType(@PathVariable Long id, @RequestBody BuildingTypeDto buildingTypeDto) {
        return ApiResponse.<BuildingTypeDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Building type"))
                .payload(buildingTypeService.updateBuildingType(id, buildingTypeDto))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_BUILDING_TYPE)
    public ApiResponse<Void> deleteBuildingType(@PathVariable Long id) {
        buildingTypeService.deleteBuildingType(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Building type"))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_BUILDING_TYPE)
    public ApiResponse<List<BuildingTypeDto>> getAllBuildingTypes() {
        return ApiResponse.<List<BuildingTypeDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building types"))
                .payload(buildingTypeService.getAllBuildingTypes())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<BuildingTypeDto>> getBuildingTypes(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingTypeDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building types"))
                .payload(buildingTypeService.getBuildingTypes(params))
                .build();
    }
}
