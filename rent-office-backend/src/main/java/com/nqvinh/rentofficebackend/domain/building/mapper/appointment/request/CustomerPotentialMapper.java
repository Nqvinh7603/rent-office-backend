/*******************************************************************************
 * Class        ：CustomerPotentialMapper
 * Created date ：2025/03/27
 * Lasted date  ：2025/03/27
 * Author       ：vinhNQ2
 * Change log   ：2025/03/27：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.request;

import com.nqvinh.rentofficebackend.domain.building.dto.CustomerPotentialDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * CustomerPotentialMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {AppointmentReqMapper.class})
public interface CustomerPotentialMapper extends CommonMapper<CustomerPotentialDto, Customer> {

    @Override
    @Mapping(target = "appointments", ignore = true)
    void partialUpdate(@MappingTarget Customer entity, CustomerPotentialDto dto);
}