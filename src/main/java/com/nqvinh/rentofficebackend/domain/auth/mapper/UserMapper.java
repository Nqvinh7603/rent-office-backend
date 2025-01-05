package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses= {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toUserDTO(User user);

    User toUser(UserDto userDTO);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.permissions", ignore = true)
    void updateUserFromDTO( @MappingTarget User user,UserDto userDTO);
}
