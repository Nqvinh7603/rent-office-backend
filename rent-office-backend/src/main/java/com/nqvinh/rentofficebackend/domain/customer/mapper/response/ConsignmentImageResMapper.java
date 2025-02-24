package com.nqvinh.rentofficebackend.domain.customer.mapper.response;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.ConsignmentImageResDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface ConsignmentImageResMapper extends CommonMapper<ConsignmentImageResDto, ConsignmentImage> {
}
