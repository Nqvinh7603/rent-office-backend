/*******************************************************************************
 * Class        ：FeeTypeController
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.application.api.fee;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.FeeTypeDto;
import com.nqvinh.rentofficebackend.domain.building.service.FeeTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FeeTypeController
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@RestController
@RequestMapping(UrlConstant.FEE_TYPES)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FeeTypeController {

    FeeTypeService feeTypeService;

    @PostMapping
    public ApiResponse<FeeTypeDto> createBuildingType(@RequestBody @Valid FeeTypeDto feeTypeDto) {
        return ApiResponse.<FeeTypeDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Fee type"))
                .payload(feeTypeService.createFeeType(feeTypeDto))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_FEE_TYPE)
    public ApiResponse<FeeTypeDto> updateFeeType(@PathVariable Long id, @RequestBody FeeTypeDto feeTypeDto) {
        return ApiResponse.<FeeTypeDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Fee type"))
                .payload(feeTypeService.updateFeeType(id, feeTypeDto))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_FEE_TYPE)
    public ApiResponse<Void> deleteFeeType(@PathVariable Long id) {
        feeTypeService.deleteFeeType(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Fee type"))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_FEE_TYPE)
    public ApiResponse<List<FeeTypeDto>> getAllFeeTypes() {
        return ApiResponse.<List<FeeTypeDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Fee types"))
                .payload(feeTypeService.getAllFeeTypes())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<FeeTypeDto>> getFeeTypes(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<FeeTypeDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Fee types"))
                .payload(feeTypeService.getFeeTypes(params))
                .build();
    }
}