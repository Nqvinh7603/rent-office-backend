package com.nqvinh.rentofficebackend.domain.building.dto.response;

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
public class BuildingResDto {
    Long buildingId;

    @NotBlank(message = "Building name is required")
    String buildingName;

    @NotBlank(message = "Consignment name is required")
    String ward; // phường

    @NotBlank(message = "District is required")
    String district; // quận

    @NotBlank(message = "City is required")
    String city; // thành phố

    @NotBlank(message = "Street is required")
    String street; // địa chỉ

    String buildingNumber;

    @NotBlank(message = "Orientation is required")
    String orientation; //Hướng

    @NotNull(message = "List image is required")
    List<BuildingImageResDto> buildingImages = List.of();

    @NotNull(message = "List status history is required")
    List<ConsignmentStatusHistoryResDto> consignmentStatusHistories = List.of();

    String buildingStatus;

    @NotNull(message = "Rental pricing is required")
    List<RentalPricingResDto> rentalPricing;

    @NotBlank(message = "Description is required")
    String description;

    String status;

    @NotNull(message = "Building type is required")
    BuildingTypeResDto buildingType;

    String rejectedReason;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    @NotNull(message = "Fee is required")
    FeeResDto fee;

    @NotBlank(message = "Number of floor is required")
    Integer numberOfFloors;

    @NotBlank(message = "Total area is required")
    Integer totalArea;

    @NotNull(message = "Payment policies is required")
    List<PaymentPolicyResDto> paymentPolicies;

    @NotNull(message = "Building units is required")
    List<BuildingUnitResDto> buildingUnits;
}
