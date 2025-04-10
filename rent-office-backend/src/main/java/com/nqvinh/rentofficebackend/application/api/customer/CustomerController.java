package com.nqvinh.rentofficebackend.application.api.customer;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerPotentialDto;
import com.nqvinh.rentofficebackend.domain.building.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping(UrlConstant.CUSTOMERS_POTENTIAL)
    public ApiResponse<CustomerDto> createPotentialCustomer(@RequestBody CustomerDto customerDto) {
        return ApiResponse.<CustomerDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Potential customer"))
                .payload(customerService.createPotentialCustomer(customerDto))
                .build();
    }

    @GetMapping(UrlConstant.CUSTOMERS_POTENTIAL)
    public ApiResponse<Page<CustomerPotentialDto>> getPotentialCustomers(@RequestParam Map<String,String> params){
        return ApiResponse.<Page<CustomerPotentialDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Potential customers"))
                .payload(customerService.getPotentialCustomers(params))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_CUSTOMERS_POTENTIAL)
    public ApiResponse<CustomerPotentialDto> updatePotentialCustomer(@PathVariable("id") Long customerId, @RequestBody CustomerPotentialDto customerDto){
        return ApiResponse.<CustomerPotentialDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Potential customer"))
                .payload(customerService.updatePotentialCustomer(customerId, customerDto))
                .build();
    }


    @DeleteMapping(UrlConstant.DELETE_CUSTOMERS_POTENTIAL)
    public ApiResponse<Void> deletePotentialCustomer(@PathVariable("id") Long customerId){
        customerService.deletePotentialCustomer(customerId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Potential customer"))
                .build();
    }

    @GetMapping(UrlConstant.GET_CUSTOMERS_POTENTIAL)
    public ApiResponse<List<CustomerDto>> getPotentialCustomers(){
        return ApiResponse.<List<CustomerDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Potential customers"))
                .payload(customerService.getPotentialCustomers())
                .build();
    }

    @GetMapping(UrlConstant.GET_CUSTOMERS_POTENTIAL_BY_ID)
    public ApiResponse<CustomerPotentialDto> getPotentialCustomersById(@PathVariable("id") Long customerId){
        return ApiResponse.<CustomerPotentialDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Potential customer"))
                .payload(customerService.getPotentialCustomersById(customerId))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_CUSTOMER)
    public ApiResponse<List<CustomerDto>> getAllCustomers() {
        return ApiResponse.<List<CustomerDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customers"))
                .payload(customerService.getAllCustomers())
                .build();
    }

    @GetMapping(UrlConstant.CUSTOMER_STATISTICS)
    public ApiResponse<Map<String, Object>> getCustomerStatistics(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer statistics"))
                .payload(customerService.getCustomerStatistics(params))
                .build();
    }

    @GetMapping(UrlConstant.CUSTOMER_STATISTICS_TIME)
    public ApiResponse<Map<String, Object>> getCustomerStatisticsByTime(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer statistics by time"))
                .payload(customerService.getCustomerStatisticsByTime(params))
                .build();
    }

    @GetMapping(UrlConstant.CUSTOMER_STATISTICS_TIME_AND_TYPE_CONSIGNMENT)
    public ApiResponse<Map<String, Object>> getCustomerStatisticsByTimeAndType(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer statistics by time and type"))
                .payload(customerService.getCustomerStatisticsByTimeAndTypeConsignment(params))
                .build();
    }

    @GetMapping(UrlConstant.CUSTOMER_STATISTICS_TIME_AND_TYPE_POTENTIAL)
    public ApiResponse<Map<String, Object>> getCustomerStatisticsByTimeAndTypePotential(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, Object>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Customer statistics by time and type potential"))
                .payload(customerService.getCustomerStatisticsByTimeAndTypePotential(params))
                .build();
    }

}
