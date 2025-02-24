package com.nqvinh.rentofficebackend.domain.customer.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsignmentImageResDto {
    Long consignmentImageId;

    @NotBlank(message = "Image url is required")
    String imgUrl;
}
