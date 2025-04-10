package com.nqvinh.rentofficebackend.application.api.building;


import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignBuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.response.CustomerResDto;
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

    @PostMapping(UrlConstant.CREATE_BUILDING_WITH_CUSTOMER)
    public ApiResponse<CustomerResDto> createBuildingWithCustomer(@RequestPart("customer") @Valid CustomerReqDto customerDto, @RequestParam("building_img") List<MultipartFile> consignmentImages) {
        return ApiResponse.<CustomerResDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Building & Customer"))
                .payload(buildingService.createBuildingWithCustomer(customerDto, consignmentImages))
                .build();
    }


    @GetMapping
    public ApiResponse<Page<BuildingDto>> getCustomerConsignments(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer consignments"))
                .payload(buildingService.getCustomerBuildings(params))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_BUILDING)
    public ApiResponse<Void> deleteBuilding(@PathVariable("id") Long buildingId) {
        buildingService.deleteBuilding(buildingId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Building"))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_BUILDING)
    public ApiResponse<BuildingDto> updateBuilding(@PathVariable("id") Long buildingId,
                                                         @RequestPart("customer") BuildingDto buildingDto,
                                                         @RequestParam(value = "building_img", required = false) List<MultipartFile> buildingImages,
                                                         @RequestParam(value = "deleted_images", required = false) List<String> deletedImages

    ) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Consignment & Customer"))
                .payload(buildingService.updateBuilding(buildingId, buildingDto, buildingImages, deletedImages))
                .build();
    }

    @GetMapping(UrlConstant.GET_BUILDING_BY_ID)
    public ApiResponse<BuildingDto> getConsignmentById(@PathVariable("id") Long buildingId) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building"))
                .payload(buildingService.getBuildingById(buildingId))
                .build();
    }

    @GetMapping(UrlConstant.VERIFY_TOKEN_BUILDING)
    public ApiResponse<Void> verifyTokenBuilding(@PathVariable("id") String buildingId, @RequestParam("token") String token) {
        buildingService.verifyTokenBuilding(buildingId, token);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Verify token building successfully")
                .build();
    }

    @GetMapping(UrlConstant.LIST_BUILDING_OF_COMPANY)
    public ApiResponse<Page<BuildingDto>> getBuildingOfCompany(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Company buildings"))
                .payload(buildingService.getBuildings(params))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_BUILDING_OF_COMPANY)
    public ApiResponse<List<BuildingDto>> getAllBuildingOfCompany() {
        return ApiResponse.<List<BuildingDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Company buildings"))
                .payload(buildingService.getAllBuildingCompany())
                .build();
    }


    @PostMapping(UrlConstant.ASSIGN_BUILDING)
    public ApiResponse<Void> assignmentBuildingToStaffs(@RequestBody AssignBuildingDto assignBuildingDto) {
        buildingService.assignmentBuildingToStaffs(assignBuildingDto);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Assign building to staffs successfully")
                .build();
    }

    @GetMapping(UrlConstant.GET_STAFFS_BY_BUILDING_ID)
    public ApiResponse<List<UserDto>> getStaffsByBuildingId(@PathVariable("id") Long buildingId) {
        return ApiResponse.<List<UserDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Get staffs by building id successfully")
                .payload(buildingService.getStaffsByBuildingId(buildingId))
                .build();
    }

    @GetMapping(UrlConstant.BUILDING_STATISTICS)
    public ApiResponse<Map<String, Object>> getBuildingStatistics(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message("Get building statistics successfully")
                .payload(buildingService.getBuildingStatistics(params))
                .build();
    }
}
