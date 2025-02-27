package com.nqvinh.rentofficebackend.application.api.customer;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlConstant.CUSTOMERS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CustomerController {

    CustomerService customerService;

    @PostMapping(UrlConstant.ASSIGN_CUSTOMER)
    public ApiResponse<Void> assignmentCustomerToStaffs(@RequestBody AssignCustomerDto assignCustomerDto) {
        customerService.assignmentCustomerToStaffs(assignCustomerDto);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Assign customer to staffs successfully")
                .build();
    }

    @GetMapping(UrlConstant.GET_STAFFS_BY_CUSTOMER_ID)
    public ApiResponse<List<UserDto>> getStaffsByCustomerId(@PathVariable("id") Long customerId) {
        return ApiResponse.<List<UserDto>>builder()
                .status(HttpStatus.OK.value())
                .message("Get staffs by customer id successfully")
                .payload(customerService.getStaffsByCustomerId(customerId))
                .build();
    }

    @GetMapping(UrlConstant.GET_CUSTOMERS_BY_REQUIRE_TYPE)
    public ApiResponse<List<CustomerDto>> getCustomersByRequireType() {
        return ApiResponse.<List<CustomerDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customers by require type"))
                .payload(customerService.getCustomersByRequireType())
                .build();
    }

}
