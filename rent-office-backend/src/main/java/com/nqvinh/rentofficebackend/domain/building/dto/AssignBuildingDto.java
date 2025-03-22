/*******************************************************************************
 * Class        ：AssignBuildingDto
 * Created date ：2025/03/18
 * Lasted date  ：2025/03/18
 * Author       ：vinhNQ2
 * Change log   ：2025/03/18：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * AssignBuildingDto
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignBuildingDto {
    BuildingDto building;
    List<UserDto> users;
}