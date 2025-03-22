/*******************************************************************************
 * Class        ：FeeReqMapper
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.FeeReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Fee;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * FeeReqMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {FeePricingReqMapper.class, FeeTypeReqMapper.class})
public interface FeeReqMapper extends CommonMapper<FeeReqDto, Fee> {
}