package com.nqvinh.rentofficebackend.application.api.building;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingLevelService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.BUILDING_LEVELS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingLevelController {

    BuildingLevelService buildingLevelService;

    @GetMapping(UrlConstant.GET_ALL_BUILDING_LEVEL)
    public ApiResponse<List<BuildingLevelDto>> getAllBuildingLevels() {
        return ApiResponse.<List<BuildingLevelDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building levels"))
                .payload(buildingLevelService.getAllBuildingLevels())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<BuildingLevelDto>> getBuildingLevels(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingLevelDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building levels"))
                .payload(buildingLevelService.getBuildingLevels(params))
                .build();
    }

    @PostMapping
    public ApiResponse<BuildingLevelDto> createBuildingLevel(@RequestBody @Valid BuildingLevelDto buildingLevelDto) {
        return ApiResponse.<BuildingLevelDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Building level"))
                .payload(buildingLevelService.createBuildingLevel(buildingLevelDto))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_BUILDING_LEVEL)
    public ApiResponse<BuildingLevelDto> updateBuildingLevel(@PathVariable Long id, @RequestBody BuildingLevelDto buildingLevelDto) {
        return ApiResponse.<BuildingLevelDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Building level"))
                .payload(buildingLevelService.updateBuildingLevel(id, buildingLevelDto))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_BUILDING_LEVEL)
    public ApiResponse<Void> deleteBuildingLevel(@PathVariable Long id) {
        buildingLevelService.deleteBuildingLevel(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Building level"))
                .build();
    }
}
