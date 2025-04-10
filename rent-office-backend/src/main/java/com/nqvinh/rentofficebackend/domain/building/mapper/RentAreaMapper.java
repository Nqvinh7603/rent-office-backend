/*******************************************************************************
 * Class        ：RentAreaMapper
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.RentAreaDto;
import com.nqvinh.rentofficebackend.domain.building.entity.RentArea;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * RentAreaMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface RentAreaMapper extends CommonMapper<RentAreaDto, RentArea> {
}