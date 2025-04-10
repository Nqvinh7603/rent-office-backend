package com.nqvinh.rentofficebackend.domain.common.mapper;

import com.nqvinh.rentofficebackend.domain.common.dto.NotificationDto;
import com.nqvinh.rentofficebackend.domain.common.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface NotificationMapper extends CommonMapper<NotificationDto, Notification> {

}
