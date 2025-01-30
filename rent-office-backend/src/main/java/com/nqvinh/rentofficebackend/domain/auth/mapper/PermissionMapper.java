package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.PermissionDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Permission;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, config = CommonMapperConfig.class)
public interface PermissionMapper extends CommonMapper<PermissionDto, Permission> {

}
