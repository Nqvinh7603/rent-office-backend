package com.nqvinh.rentofficebackend.domain.building.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingImageDto {
    Long buildingImageId;

    String imgUrl;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

}
