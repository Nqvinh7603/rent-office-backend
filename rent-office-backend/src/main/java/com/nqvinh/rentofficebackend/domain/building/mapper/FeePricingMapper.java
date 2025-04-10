/*******************************************************************************
 * Class        ：FeePricingMapper
 * Created date ：2025/03/13
 * Lasted date  ：2025/03/13
 * Author       ：vinhNQ2
 * Change log   ：2025/03/13：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.FeePricingDto;
import com.nqvinh.rentofficebackend.domain.building.entity.FeePricing;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * FeePricingMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface FeePricingMapper extends CommonMapper<FeePricingDto, FeePricing> {
}