package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class}, config = CommonMapperConfig.class)
public interface RoleMapper extends CommonMapper<RoleDto, Role> {
}
