/*******************************************************************************
 * Class        ：BuildingLevelClientResMapper
 * Created date ：2025/03/20
 * Lasted date  ：2025/03/20
 * Author       ：vinhNQ2
 * Change log   ：2025/03/20：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.client;

import com.nqvinh.rentofficebackend.domain.building.dto.response.client.BuildingLevelClientRes;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * BuildingLevelClientResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", uses = {BuildingClientResMapper.class}, config = CommonMapperConfig.class)
public interface BuildingLevelClientResMapper extends CommonMapper<BuildingLevelClientRes, BuildingLevel> {
}