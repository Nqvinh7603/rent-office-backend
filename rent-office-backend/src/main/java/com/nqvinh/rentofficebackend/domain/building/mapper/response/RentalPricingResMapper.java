/*******************************************************************************
 * Class        ：RentalPricingResMapper
 * Created date ：2025/03/11
 * Lasted date  ：2025/03/11
 * Author       ：vinhNQ2
 * Change log   ：2025/03/11：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.RentalPricingResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.RentalPricing;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * RentalPricingResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface RentalPricingResMapper extends CommonMapper<RentalPricingResDto, RentalPricing> {
}