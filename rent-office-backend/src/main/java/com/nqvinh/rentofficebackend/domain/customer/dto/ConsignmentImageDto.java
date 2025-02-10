package com.nqvinh.rentofficebackend.domain.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsignmentImageDto {

    Long consignmentImageId;

    @NotBlank(message = "Image url is required")
    String imgUrl;
}
