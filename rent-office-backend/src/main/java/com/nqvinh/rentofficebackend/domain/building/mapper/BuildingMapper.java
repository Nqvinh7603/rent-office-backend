package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {BuildingMapper.class, BuildingTypeMapper.class, BuildingUnitMapper.class, FeePriceMapper.class},
        config = CommonMapperConfig.class)
public interface BuildingMapper extends CommonMapper<BuildingDto, Building> {
}
