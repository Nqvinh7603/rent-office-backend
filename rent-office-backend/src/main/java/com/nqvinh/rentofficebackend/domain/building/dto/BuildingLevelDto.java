package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingLevelDto {
    Long buildingLevelId;

    @NotBlank(message = "Building level code is required")
    String buildingLevelCode;

    @NotBlank(message = "Building level name is required")
    String buildingLevelName;

    String description;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
