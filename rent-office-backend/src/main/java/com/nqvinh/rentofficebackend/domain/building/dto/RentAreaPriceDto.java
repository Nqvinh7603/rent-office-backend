package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RentAreaPriceDto {
    Long rentAreaPriceId;

    @NotNull(message = "Price is required")
    BigDecimal price;

    @NotNull(message = "Valid from is required")
    LocalDate validFrom;
    LocalDate validTo;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
