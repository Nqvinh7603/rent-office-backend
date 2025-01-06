package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses= {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role.permissions", ignore = true)
    void updateUserFromDto( @MappingTarget User user,UserDto userDto);
}
