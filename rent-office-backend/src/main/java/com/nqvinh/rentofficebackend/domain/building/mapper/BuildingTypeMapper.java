package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingTypeMapper extends CommonMapper<BuildingTypeDto, BuildingType> {
}
