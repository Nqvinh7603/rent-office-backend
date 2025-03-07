package com.nqvinh.rentofficebackend.domain.customer.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    void assignmentCustomerToStaffs(AssignCustomerDto assignCustomerDto);
    List<UserDto> getStaffsByCustomerId(Long customerId);
    List<CustomerDto> getCustomersByRequireType();
    Customer findOrCreateCustomer(CustomerReqDto customerReqDto);

    //khách hàng tiềm năng
    CustomerDto createPotentialCustomer(CustomerDto customerDto);
    CustomerDto updatePotentialCustomer(Long customerId, CustomerDto customerDto);
    void deletePotentialCustomer(Long customerId);
    Page<CustomerDto> getPotentialCustomers(Map<String, String> params);
    List<CustomerDto> getPotentialCustomers();
}
