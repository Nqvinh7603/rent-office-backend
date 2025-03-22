package com.nqvinh.rentofficebackend.domain.building.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingImageResDto {
    Long buildingImageId;

    @NotBlank(message = "Image url is required")
    String imgUrl;
}
