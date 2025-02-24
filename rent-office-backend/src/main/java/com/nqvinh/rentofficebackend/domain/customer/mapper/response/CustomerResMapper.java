package com.nqvinh.rentofficebackend.domain.customer.mapper.response;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.CustomerResDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentResMapper.class})
public interface CustomerResMapper extends CommonMapper<CustomerResDto, Customer> {

}
