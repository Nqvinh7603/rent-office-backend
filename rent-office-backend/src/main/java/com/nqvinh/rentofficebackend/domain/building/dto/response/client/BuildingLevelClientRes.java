/*******************************************************************************
 * Class        ：BuildingLevelClientRes
 * Created date ：2025/03/20
 * Lasted date  ：2025/03/20
 * Author       ：vinhNQ2
 * Change log   ：2025/03/20：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.response.client;

import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BuildingLevelClientRes
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
public class BuildingLevelClientRes {
    Long buildingLevelId;

    String buildingLevelCode;

    String buildingLevelName;

    String description;

    List<BuildingClientRes> buildings;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}