package com.nqvinh.rentofficebackend.application.api.customer;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.CustomerResDto;
import com.nqvinh.rentofficebackend.domain.customer.service.ConsignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.CONSIGNMENTS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ConsignmentController {
    ConsignmentService consignmentService;

    @PostMapping
    public ApiResponse<CustomerResDto> createConsignment(@RequestPart("customer") @Valid CustomerReqDto customerDto, @RequestParam("consignment_img") List<MultipartFile> consignmentImages) {
        return ApiResponse.<CustomerResDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Consignment & Customer"))
                .payload(consignmentService.createConsignment(customerDto, consignmentImages))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<ConsignmentDto>> getCustomerConsignments(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<ConsignmentDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer consignments"))
                .payload(consignmentService.getCustomerConsignments(params))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_CONSIGNMENT)
    public ApiResponse<Void> deleteConsignment(@PathVariable("id") Long consignmentId) {
        consignmentService.deleteConsignment(consignmentId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Consignment"))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_CONSIGNMENT)
    public ApiResponse<ConsignmentDto> updateConsignment(@PathVariable("id") Long consignmentId,
                                                         @RequestPart("customer") ConsignmentDto consignmentDto,
                                                         @RequestParam(value = "consignment_img", required = false) List<MultipartFile> consignmentImages,
                                                            @RequestParam(value = "deleted_images", required = false) List<String> deletedImages

    ) {
        return ApiResponse.<ConsignmentDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Consignment & Customer"))
                .payload(consignmentService.updateConsignment(consignmentId, consignmentDto, consignmentImages, deletedImages))
                .build();
    }

    @GetMapping(UrlConstant.GET_CONSIGNMENT_BY_ID)
    public ApiResponse<ConsignmentDto> getConsignmentById(@PathVariable("id") Long consignmentId) {
        return ApiResponse.<ConsignmentDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Consignment"))
                .payload(consignmentService.getConsignmentById(consignmentId))
                .build();
    }

    @GetMapping(UrlConstant.VERIFY_TOKEN_CONSIGNMENT)
    public ApiResponse<Void> verifyTokenConsignment(@PathVariable("id") String consignmentId, @RequestParam("token") String token) {
        consignmentService.verifyTokenConsignment(consignmentId, token);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Verify token consignment successfully")
                .build();
    }

}
