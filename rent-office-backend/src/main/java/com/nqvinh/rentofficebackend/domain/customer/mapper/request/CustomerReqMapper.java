package com.nqvinh.rentofficebackend.domain.customer.mapper.request;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentReqMapper.class})
public interface CustomerReqMapper extends CommonMapper<CustomerReqDto, Customer> {
}
