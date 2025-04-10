package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface CustomerMapper extends CommonMapper<CustomerDto, Customer> {

}