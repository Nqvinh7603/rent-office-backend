/*******************************************************************************
 * Class        ：BuildingUnitReqDto
 * Created date ：2025/03/24
 * Lasted date  ：2025/03/24
 * Author       ：vinhNQ2
 * Change log   ：2025/03/24：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BuildingUnitReqDto
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
public class BuildingUnitReqDto {
    Long buildingUnitId;

    Integer floor;

    String status;

    String unitName;

    List<RentAreaReqDto> rentAreas;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    @NotNull(message = "Rental pricing is required")
    List<RentalPricingReqDto> rentalPricing;

}