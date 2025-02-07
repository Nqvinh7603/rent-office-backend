package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BuildingDto {
    Long buildingId;

    @NotBlank(message = "Building name is required")
    String buildingName;

    @NotBlank(message = "Ward is required")
    String ward;

    @NotBlank(message = "District is required")
    String district;

    @NotBlank(message = "City is required")
    String street;

    @NotBlank(message = "Building number is required")
    String buildingNumber;

    String workingHours;

    String orientation;

    @NotBlank(message = "Status is required")
    String status;

    @NotNull(message = "Building level is required")
    BuildingLevelDto buildingLevel;

    @NotNull(message = "Building type is required")
    BuildingTypeDto buildingType;

    List<BuildingImageDto> buildingImages = new ArrayList<>();

    List<BuildingUnitDto> buildingUnits;

    List<FeePriceDto> feePrices;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
