/*******************************************************************************
 * Class        ：BuildingClientController
 * Created date ：2025/03/21
 * Lasted date  ：2025/03/21
 * Author       ：vinhNQ2
 * Change log   ：2025/03/21：01-00 vinhNQ2 create a new
******************************************************************************/
package com.nqvinh.rentofficebackend.application.api.building.client;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingClientService;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * BuildingClientController
 *
 * @version 01-00
 * @since 01-00
 * @author vinhNQ2
 */
@RestController
@RequestMapping(UrlConstant.BUILDING_CLIENTS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingClientController {
    BuildingClientService buildingClientService;
    BuildingService buildingService;

    @GetMapping
    public ApiResponse<Page<BuildingDto>> getBuildingOfCompany(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BuildingDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Company buildings"))
                .payload(buildingClientService.getBuildingClients(params))
                .build();
    }

    @GetMapping(UrlConstant.GET_BUILDING_CLIENT_BY_ID)
    public ApiResponse<BuildingDto> getConsignmentById(@PathVariable("id") Long buildingId) {
        return ApiResponse.<BuildingDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Building"))
                .payload(buildingService.getBuildingById(buildingId))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_STREET)
    public ApiResponse<List<String>> getAllStreetByWardNameAndDistrictName(@RequestParam("ward") String wardName, @RequestParam("district") String districtName) {
        return ApiResponse.<List<String>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Street"))
                .payload(buildingClientService.getAllStreetByWardNameAndDistrictName(wardName, districtName))
                .build();
    }
}