/*******************************************************************************
 * Class        ：RentAreaResMapper
 * Created date ：2025/03/24
 * Lasted date  ：2025/03/24
 * Author       ：vinhNQ2
 * Change log   ：2025/03/24：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.RentAreaResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.RentArea;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * RentAreaResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface RentAreaResMapper extends CommonMapper<RentAreaResDto, RentArea> {
}