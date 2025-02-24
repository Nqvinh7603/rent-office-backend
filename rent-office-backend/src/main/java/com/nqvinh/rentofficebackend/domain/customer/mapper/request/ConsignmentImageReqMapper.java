package com.nqvinh.rentofficebackend.domain.customer.mapper.request;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.ConsignmentImageReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface ConsignmentImageReqMapper extends CommonMapper<ConsignmentImageReqDto, ConsignmentImage> {
}
