/*******************************************************************************
 * Class        ：BuildingTypeResMapper
 * Created date ：2025/03/10
 * Lasted date  ：2025/03/10
 * Author       ：vinhNQ2
 * Change log   ：2025/03/10：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.BuildingTypeResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * BuildingTypeResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingTypeResMapper extends CommonMapper<BuildingTypeResDto, BuildingType> {
}