/*******************************************************************************
 * Class        ：FeePricingDto
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * FeePricingDto
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
public class FeePricingDto {
    Long feePricingId;

    String priceUnit;

    BigDecimal priceValue;

    String description;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}