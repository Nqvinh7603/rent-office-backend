package com.nqvinh.rentofficebackend.domain.customer.mapper.request;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.ConsignmentReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentImageReqMapper.class})
public interface ConsignmentReqMapper extends CommonMapper<ConsignmentReqDto, Consignment> {
}
