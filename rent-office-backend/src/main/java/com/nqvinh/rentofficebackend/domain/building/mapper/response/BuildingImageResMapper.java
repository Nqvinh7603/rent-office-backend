package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.BuildingImageResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingImageResMapper extends CommonMapper<BuildingImageResDto, BuildingImage> {
}
