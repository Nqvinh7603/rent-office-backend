package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.FeePriceDto;
import com.nqvinh.rentofficebackend.domain.building.entity.FeePrice;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FeeMapper.class}, config = CommonMapperConfig.class)
public interface FeePriceMapper extends CommonMapper<FeePriceDto, FeePrice> {
}
