package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerPotentialDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    void assignmentCustomerToStaffs(AssignCustomerDto assignCustomerDto);
    List<UserDto> getStaffsByCustomerId(Long customerId);
    List<CustomerDto> getCustomersByRequireType();
    Customer findOrCreateCustomer(CustomerReqDto customerReqDto);

    //khách hàng tiềm năng
    CustomerDto createPotentialCustomer(CustomerDto customerDto);
    CustomerPotentialDto updatePotentialCustomer(Long customerId, CustomerPotentialDto customerDto);
    void deletePotentialCustomer(Long customerId);
    Page<CustomerPotentialDto> getPotentialCustomers(Map<String, String> params);
    List<CustomerDto> getPotentialCustomers();
    CustomerPotentialDto getPotentialCustomersById(Long customerId);
    List<CustomerDto> getAllCustomers();
}
