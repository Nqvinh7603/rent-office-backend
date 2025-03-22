package com.nqvinh.rentofficebackend.domain.building.dto;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignCustomerDto {
    CustomerDto customer;
    List<UserDto> users;
}
