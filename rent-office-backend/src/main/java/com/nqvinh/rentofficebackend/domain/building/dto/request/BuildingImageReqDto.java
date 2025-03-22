package com.nqvinh.rentofficebackend.domain.building.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingImageReqDto {
    Long buildingImageId;

    @NotBlank(message = "Image url is required")
    String imgUrl;
}
