/*******************************************************************************
 * Class        ：FeeDto
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FeeDto
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeeDto {
    Long feeId;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    @NotNull(message = "Fee pricing is required")
    List<FeePricingDto> feePricing;

    @NotNull(message = "Fee type is required")
    FeeTypeDto feeType;
}