package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingDto {

    Long buildingId;

    @NotBlank(message = "Consignment name is required")
    String ward; // phường

    @NotBlank(message = "District is required")
    String district; // quận

    @NotBlank(message = "City is required")
    String city; // thành phố

    @NotBlank(message = "Street is required")
    String street; // địa chỉ

    @NotBlank(message = "Orientation is required")
    String orientation; //Hướng

    @NotNull(message = "List image is required")
    List<BuildingImageDto> buildingImages = List.of();

//    @NotNull(message = "Rental pricing is required")
//    List<RentalPricingReqDto> rentalPricing;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Building type is required")
    BuildingTypeDto buildingType;

    BuildingLevelDto buildingLevel;

    @NotNull(message = "Status is required")
    List<ConsignmentStatusHistoryDto> consignmentStatusHistories = List.of();

    String buildingStatus;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    @NotNull(message = "Customer is required")
    CustomerDto customer;

    @NotBlank(message = "Building name is required")
    String buildingName;

    @NotBlank(message = "Building number is required")
    String buildingNumber;

    @NotNull(message = "Fee is required")
    List<FeeDto> fees;

    @NotBlank(message = "Number of floor is required")
    Integer numberOfFloors;

    @NotBlank(message = "Total area is required")
    Integer totalArea;

    @NotNull(message = "Payment policy is required")
    List<PaymentPolicyDto> paymentPolicies;

    List<BuildingDetailDto> buildingDetails;

    List<BuildingUnitDto> buildingUnits;
}
