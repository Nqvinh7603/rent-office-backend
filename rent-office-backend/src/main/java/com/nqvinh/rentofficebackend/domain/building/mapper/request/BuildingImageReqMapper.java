package com.nqvinh.rentofficebackend.domain.building.mapper.request;


import com.nqvinh.rentofficebackend.domain.building.dto.request.BuildingImageReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingImageReqMapper extends CommonMapper<BuildingImageReqDto, BuildingImage> {
}
