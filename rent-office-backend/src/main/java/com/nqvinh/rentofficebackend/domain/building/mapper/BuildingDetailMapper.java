/*******************************************************************************
 * Class        ：BuildingDetailMapper
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDetailDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingDetail;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * BuildingDetailMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingDetailMapper extends CommonMapper<BuildingDetailDto, BuildingDetail> {
}