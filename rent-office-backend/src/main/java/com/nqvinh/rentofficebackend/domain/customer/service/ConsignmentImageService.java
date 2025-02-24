package com.nqvinh.rentofficebackend.domain.customer.service;

import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ConsignmentImageService {
    void updateConsignmentImages(Consignment consignment, List<MultipartFile> consignmentImages);
    List<Consignment> convertConsignmentDtoToEntities(CustomerReqDto customerReqDto, List<String> uploadedUrls, Customer customer);
    List<String> uploadConsignmentImages(CustomerReqDto customerReqDto, List<MultipartFile> consignmentImages);
}
