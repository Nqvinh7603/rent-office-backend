package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignBuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.common.dto.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createBuildingNotification(UserDto userDto, Customer savedCustomer);
    void markAllNotificationsAsRead(UUID userId);
    void markNotificationAsRead(UUID userId,Long notificationId);
    void deleteNotification(List<Long> notificationId);
    List<NotificationDto> getNotifications(UUID userId);
    void updateInfoBuildingNotification(UserDto userDto, BuildingDto savedCustomer);
    void assignCustomerToStaffs(AssignCustomerDto assignCustomerDto);
    void assignBuildingToStaffs(AssignBuildingDto assignBuildingDto);
    void createPotentialCustomerNotification(UserDto userDto, CustomerDto savedCustomer);
    void createAppointment(UserDto userDto, Customer savedCustomer);
}
