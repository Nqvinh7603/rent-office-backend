package com.nqvinh.rentofficebackend.domain.customer.service;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomerWithConsignment(CustomerDto customerDto, List<MultipartFile> consignmentImages);
    void assignmentCustomerToStaffs(AssignCustomerDto assignCustomerDto);
    List<UserDto> getStaffsByCustomerId(Long customerId);
}
