package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.common.dto.NotificationDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void createConsignmentNotification(UserDto userDto, Customer savedCustomer);
    void markAllNotificationsAsRead(UUID userId);
    void markNotificationAsRead(UUID userId,Long notificationId);
    void deleteNotification(List<Long> notificationId);
    List<NotificationDto> getNotifications(UUID userId);
    void updateInfoConsignmentNotification(UserDto userDto, CustomerDto savedCustomer);
    void assignCustomerToStaffs(AssignCustomerDto assignCustomerDto);
}
