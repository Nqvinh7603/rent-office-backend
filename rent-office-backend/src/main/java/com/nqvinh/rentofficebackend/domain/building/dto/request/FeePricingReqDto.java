/*******************************************************************************
 * Class        ：FeePricingReqDto
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * FeePricingReqDto
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
public class FeePricingReqDto {
    Long feePricingId;

    String priceUnit;

    BigDecimal priceValue;

    String description;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}