package com.nqvinh.rentofficebackend.domain.customer.mapper;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentImageDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface ConsignmentImageMapper extends CommonMapper<ConsignmentImageDto, ConsignmentImage> {
}
