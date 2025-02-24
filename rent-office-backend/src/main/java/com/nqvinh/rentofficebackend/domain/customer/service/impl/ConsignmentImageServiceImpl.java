package com.nqvinh.rentofficebackend.domain.customer.service.impl;

import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.ConsignmentImageReqDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentImage;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import com.nqvinh.rentofficebackend.domain.customer.mapper.request.ConsignmentReqMapper;
import com.nqvinh.rentofficebackend.domain.customer.service.ConsignmentImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConsignmentImageServiceImpl implements ConsignmentImageService {

    ImageService imageService;
    ConsignmentReqMapper consignmentReqMapper;

    @Override
    @SneakyThrows
    public void updateConsignmentImages(Consignment consignment, List<MultipartFile> consignmentImages) {
        List<String> existingUrls = consignment.getConsignmentImages().stream()
                .map(ConsignmentImage::getImgUrl)
                .collect(Collectors.toList());
        List<String> uploadedUrls = imageService.handleImageUpload(consignmentImages, existingUrls).get();
        uploadedUrls.forEach(url -> {
            if (consignment.getConsignmentImages().stream().noneMatch(image -> image.getImgUrl().equals(url))) {
                consignment.getConsignmentImages().add(ConsignmentImage.builder()
                        .imgUrl(url)
                        .consignment(consignment)
                        .build());
            }
        });

    }

    @Override
    @SneakyThrows
    public List<String> uploadConsignmentImages(CustomerReqDto customerReqDto, List<MultipartFile> consignmentImages){
        if (consignmentImages == null || consignmentImages.isEmpty()) return new ArrayList<>();
        return imageService.handleImageUpload(
                consignmentImages,
                customerReqDto.getConsignments().stream()
                        .flatMap(consignment -> consignment.getConsignmentImages().stream())
                        .map(ConsignmentImageReqDto::getImgUrl)
                        .collect(Collectors.toList())
        ).get();
    }

    @Override
    public List<Consignment> convertConsignmentDtoToEntities(CustomerReqDto customerReqDto, List<String> uploadedUrls, Customer customer) {
        return customerReqDto.getConsignments().stream()
                .map(consignmentDto -> {
                    Consignment newConsignment = consignmentReqMapper.toEntity(consignmentDto);
                    newConsignment.setCustomer(customer);

                    List<ConsignmentImage> consignmentImageEntities = uploadedUrls.stream()
                            .map(imgUrl -> ConsignmentImage.builder().imgUrl(imgUrl).consignment(newConsignment).build())
                            .collect(Collectors.toList());

                    newConsignment.setConsignmentImages(consignmentImageEntities);
                    return newConsignment;
                })
                .collect(Collectors.toList());
    }

}
