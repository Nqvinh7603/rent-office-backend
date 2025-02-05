package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.RentAreaPriceDto;
import com.nqvinh.rentofficebackend.domain.building.entity.RentAreaPrice;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface RentAreaPriceMapper extends CommonMapper<RentAreaPriceDto, RentAreaPrice> {
}
