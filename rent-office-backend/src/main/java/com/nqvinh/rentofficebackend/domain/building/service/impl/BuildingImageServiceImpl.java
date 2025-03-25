package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.domain.building.constant.BuildingUnitStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.ConsignmentStatus;
import com.nqvinh.rentofficebackend.domain.building.dto.request.BuildingImageReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.mapper.request.BuildingReqMapper;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingImageService;
import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.CloudinaryUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BuildingImageServiceImpl implements BuildingImageService {

    ImageService imageService;
    BuildingReqMapper buildingReqMapper;
    CloudinaryUtils cloudinaryUtils;

    @Override
    @SneakyThrows
    public void updateBuildingImages(Building building, List<MultipartFile> consignmentImages) {
        List<String> existingUrls = building.getBuildingImages().stream()
                .map(BuildingImage::getImgUrl)
                .collect(Collectors.toList());
        List<String> uploadedUrls = imageService.handleImageUpload(consignmentImages, existingUrls).get();
        uploadedUrls.forEach(url -> {
            if (building.getBuildingImages().stream().noneMatch(image -> image.getImgUrl().equals(url))) {
                building.getBuildingImages().add(BuildingImage.builder()
                        .imgUrl(url)
                        .building(building)
                        .build());
            }
        });

    }

   @Override
    public void deleteBuildingImages(Building building, List<String> deletedImages) {
        List<BuildingImage> images = building.getBuildingImages();
        List<BuildingImage> imagesToRemove = new ArrayList<>();
        deletedImages.forEach(deletedImage -> {
            images.stream()
                  .filter(image -> image.getImgUrl().equals(deletedImage))
                  .findFirst()
                  .ifPresent(imagesToRemove::add);
            try {
                cloudinaryUtils.deleteFileFromCloudinary(deletedImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        images.removeAll(imagesToRemove);
       building.setBuildingImages(images);
    }

    @Override
    @SneakyThrows
    public List<String> uploadBuildingImages(CustomerReqDto customerReqDto, List<MultipartFile> buildingImages){
        if (buildingImages == null || buildingImages.isEmpty()) return new ArrayList<>();
        return imageService.handleImageUpload(
                buildingImages,
                customerReqDto.getBuildings().stream()
                        .flatMap(building -> building.getBuildingImages().stream())
                        .map(BuildingImageReqDto::getImgUrl)
                        .collect(Collectors.toList())
        ).get();
    }

    @Override
    public List<Building> convertBuildingDtoToEntities(CustomerReqDto customerReqDto, List<String> uploadedUrls, Customer customer) {
        return customerReqDto.getBuildings().stream()
                .map(buildingDto -> {
                    Building newBuilding = buildingReqMapper.toEntity(buildingDto);
                    newBuilding.setCustomer(customer);

                    newBuilding.setBuildingType(newBuilding.getBuildingType());

                    List<BuildingUnit> buildingUnits = buildingDto.getBuildingUnits().stream()
                                .map(buildingUnitDto -> {
                                    BuildingUnit buildingUnit = BuildingUnit.builder()
                                        .unitName(buildingUnitDto.getUnitName())
                                        .floor(buildingUnitDto.getFloor())
                                        .building(newBuilding)
                                        .build();

                                    buildingUnit.setRentAreas(buildingUnitDto.getRentAreas().stream()
                                        .map(rentAreaDto -> RentArea.builder()
                                            .area(rentAreaDto.getArea())
                                            .buildingUnit(buildingUnit)
                                            .build())
                                        .collect(Collectors.toList()));

                                    buildingUnit.setRentalPricing(buildingUnitDto.getRentalPricing().stream()
                                        .map(rentalPricingDto -> RentalPricing.builder()
                                            .price(rentalPricingDto.getPrice())
                                            .buildingUnit(buildingUnit)
                                            .build())
                                        .collect(Collectors.toList()));

                                    return buildingUnit;
                                })
                                .collect(Collectors.toList());

                    List<PaymentPolicy> paymentPolicies = buildingDto.getPaymentPolicies().stream()
                            .map(paymentPolicyDto -> PaymentPolicy.builder()
                                    .depositTerm(paymentPolicyDto.getDepositTerm())
                                    .paymentCycle(paymentPolicyDto.getPaymentCycle())
                                    .building(newBuilding)
                                    .build())
                            .collect(Collectors.toList());

                   List<Fee> fees = buildingDto.getFees().stream()
                                .map(feeDto -> {
                                    Fee fee = Fee.builder()
                                        .feeType(FeeType.builder().feeTypeId(feeDto.getFeeType().getFeeTypeId()).build())
                                        .building(newBuilding)
                                        .build();
                                    List<FeePricing> feePricingList = feeDto.getFeePricing().stream()
                                        .map(feePricingDto -> FeePricing.builder()
                                            .priceValue(feePricingDto.getPriceValue())
                                            .priceUnit(feePricingDto.getPriceUnit())
                                            .description(feePricingDto.getDescription())
                                            .fee(fee)
                                            .build())
                                        .collect(Collectors.toList());
                                    fee.setFeePricing(feePricingList);
                                    return fee;
                                })
                                .collect(Collectors.toList());

                    List<ConsignmentStatusHistory> buildingStatusHistories = buildingDto.getConsignmentStatusHistories().stream()
                            .map(buildingStatusHistoryDto -> ConsignmentStatusHistory.builder()
                                    .status(ConsignmentStatus.valueOf(buildingStatusHistoryDto.getStatus()))
                                    .building(newBuilding)
                                    .build())
                            .collect(Collectors.toList());
                    List<BuildingImage> buildingImageEntities = uploadedUrls.stream()
                            .map(imgUrl -> BuildingImage.builder().imgUrl(imgUrl).building(newBuilding).build())
                            .collect(Collectors.toList());

                    newBuilding.setBuildingImages(buildingImageEntities);
                    newBuilding.setConsignmentStatusHistories(buildingStatusHistories);
                    newBuilding.setBuildingUnits(buildingUnits);
                    newBuilding.setFees(fees);
                    newBuilding.setPaymentPolicies(paymentPolicies);

                    return newBuilding;
                })
                .collect(Collectors.toList());
    }

}
