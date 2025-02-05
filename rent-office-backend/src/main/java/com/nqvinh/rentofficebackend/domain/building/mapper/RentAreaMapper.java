package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.RentAreaDto;
import com.nqvinh.rentofficebackend.domain.building.entity.RentArea;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RentAreaPriceMapper.class}, config = CommonMapperConfig.class)
public interface RentAreaMapper extends CommonMapper<RentAreaDto, RentArea> {
}
