package com.nqvinh.rentofficebackend.domain.customer.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsignmentResDto {
    Long consignmentId;

    @NotBlank(message = "Consignment name is required")
    String ward; // phường

    @NotBlank(message = "District is required")
    String district; // quận

    @NotBlank(message = "City is required")
    String city; // thành phố

    @NotBlank(message = "Street is required")
    String street; // địa chỉ

    @NotNull(message = "List image is required")
    List<ConsignmentImageResDto> consignmentImages = List.of();

    @NotNull(message = "List status history is required")
    List<ConsignmentStatusHistoryResDto> consignmentStatusHistories = List.of();

    @NotNull(message = "Price is required")
    BigDecimal price;

    @NotBlank(message = "Description is required")
    String description;

    String status;

    String buildingType;

    String rejectedReason;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
