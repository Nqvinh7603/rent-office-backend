/*******************************************************************************
 * Class        ：BuildingTypeResDto
 * Created date ：2025/03/10
 * Lasted date  ：2025/03/10
 * Author       ：vinhNQ2
 * Change log   ：2025/03/10：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * BuildingTypeResDto
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
public class BuildingTypeResDto {
    Long buildingTypeId;

    @NotBlank(message = "Building type name is required")
    String buildingTypeName;

    @NotBlank(message = "Building type code is required")
    String buildingTypeCode;

    @NotBlank(message = "Description is required")
    String description;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}