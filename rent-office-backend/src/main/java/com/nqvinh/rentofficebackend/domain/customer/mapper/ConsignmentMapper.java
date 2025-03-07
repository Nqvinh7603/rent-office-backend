package com.nqvinh.rentofficebackend.domain.customer.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentImageMapper.class, CustomerMapper.class, ConsignmentStatusHistoryMapper.class})
public interface ConsignmentMapper extends CommonMapper<ConsignmentDto, Consignment> {

    @Override
    @Mapping(target = "consignmentStatusHistories", ignore = true)
    void partialUpdate(@MappingTarget Consignment entity, ConsignmentDto dto);
}
