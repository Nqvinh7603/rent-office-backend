package com.nqvinh.rentofficebackend.domain.customer.mapper.response;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.ConsignmentResDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentImageResMapper.class})
public interface ConsignmentResMapper extends CommonMapper<ConsignmentResDto, Consignment> {
}
