package com.nqvinh.rentofficebackend.domain.customer.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.CustomerResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ConsignmentService {
    CustomerResDto createConsignment(CustomerReqDto customerReqDto, List<MultipartFile> consignmentImages);
    Page<ConsignmentDto> getCustomerConsignments(Map<String, String> params);
    void deleteConsignment(Long consignmentId);
    ConsignmentDto updateConsignment(Long consignmentId, ConsignmentDto consignmentDto, List<MultipartFile> consignmentImages);
    ConsignmentDto getConsignmentById(Long consignmentId);
    void verifyTokenConsignment(String consignmentId,String token);
}
