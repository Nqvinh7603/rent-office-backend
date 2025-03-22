package com.nqvinh.rentofficebackend.domain.building.mapper.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {BuildingReqMapper.class})
public interface CustomerReqMapper extends CommonMapper<CustomerReqDto, Customer> {
}
