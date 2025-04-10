/*******************************************************************************
 * Class        ：BuildingDetailDto
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * BuildingDetailDto
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
public class BuildingDetailDto {
    Long buildingDetailId;

    String section;

    String content;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}