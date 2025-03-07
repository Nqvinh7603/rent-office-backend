package com.nqvinh.rentofficebackend.domain.common.service.impl;

import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.dto.NotificationDto;
import com.nqvinh.rentofficebackend.domain.common.entity.Notification;
import com.nqvinh.rentofficebackend.domain.common.event.NotiEvent;
import com.nqvinh.rentofficebackend.domain.common.mapper.NotificationMapper;
import com.nqvinh.rentofficebackend.domain.common.repository.NotificationRepository;
import com.nqvinh.rentofficebackend.domain.common.service.NotiProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    NotiProducer notiProducer;
    UserMapper userMapper;
    NotificationMapper notificationMapper;

    @Override
    public void createConsignmentNotification(UserDto userDto, Customer savedCustomer) {
        Long consignmentId = savedCustomer.getConsignments().getFirst().getConsignmentId();
        String message = "Khách hàng " + savedCustomer.getCustomerName() + " đã ký gửi tài sản mới";

        Notification notification = Notification.builder()
                .status(false)
                .user(userMapper.toEntity(userDto))
                .consignmentId(consignmentId)
                .message(message)
                .build();
        notificationRepository.save(notification);

        var notificationEvent = NotiEvent.builder()
                .status(false)
                .consignmentId(consignmentId)
                .userId(List.of(userDto.getUserId()))
                .message(message)
                .createdAt(notification.getCreatedAt())
                .code(MessageCode.NOTIFICATION_CREATE_CONSIGNMENT.getCode())
                .type(MailType.PENDING_CONSIGNMENT.getType())
                .build();
        notiProducer.sendNotiCreateConsignment(notificationEvent);
    }

    @Override
    public void updateInfoConsignmentNotification(UserDto userDto, ConsignmentDto savedConsignment) {
        String message = "Khách hàng " + savedConsignment.getCustomer().getCustomerName() + " đã cập nhật thông tin tài sản ký gửi";

        Notification notification = Notification.builder()
                .status(false)
                .user(userMapper.toEntity(userDto))
                .consignmentId(savedConsignment.getConsignmentId())
                .message(message)
                .build();
        notificationRepository.save(notification);

        var notificationEvent = NotiEvent.builder()
                .status(false)
                .consignmentId(savedConsignment.getConsignmentId())
                .userId(List.of(userDto.getUserId()))
                .message(message)
                .createdAt(notification.getCreatedAt())
                .code(MessageCode.NOTIFICATION_CREATE_CONSIGNMENT.getCode())
                .type(MailType.PENDING_CONSIGNMENT.getType())
                .build();
        notiProducer.sendNotiUpdateConsignment(notificationEvent);
    }
//boor sung customer id...
    @Override
    public void assignCustomerToStaffs(AssignCustomerDto assignCustomerDto) {
        String message = "Bạn đã được gán khách hàng " + assignCustomerDto.getCustomer().getCustomerName();

        List<Notification> notifications = userMapper.toEntityList(assignCustomerDto.getUsers()).stream().map(user ->
            Notification.builder()
                    .status(false)
                    .user(user)
                    .message(message)
                    .build()
        ).collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

        var notificationEvent = NotiEvent.builder()
                .status(false)
                //.customerId(assignCustomerDto.getCustomer().getCustomerId())
                .userId(assignCustomerDto.getUsers().stream().map(UserDto::getUserId).collect(Collectors.toList()))
                .message(message)
                .createdAt(LocalDateTime.now())
                .code(MessageCode.NOTIFICATION_ASSIGN_CUSTOMER.getCode())
                .type(MailType.NOTIFICATION_ASSIGN_CUSTOMER.getType())
                .build();
        notiProducer.sendNotiAssignCustomer(notificationEvent);
    }

    @Override
    public void createPotentialCustomerNotification(UserDto userDto, CustomerDto savedCustomer) {
        String message = "Khách hàng " + savedCustomer.getCustomerName() + " có nhu cầu thuê tài sản";

        Notification notification = Notification.builder()
                .status(false)
                .user(userMapper.toEntity(userDto))
                .customerId(savedCustomer.getCustomerId())
                .message(message)
                .build();
        notificationRepository.save(notification);

        var notificationEvent = NotiEvent.builder()
                .status(false)
                .userId(List.of(userDto.getUserId()))
                .message(message)
                .createdAt(notification.getCreatedAt())
                .code(MessageCode.NOTIFICATION_CREATE_CUSTOMER_POTENTIAL.getCode())
                .type(MailType.NOTIFICATION_CREATE_CUSTOMER_POTENTIAL.getType())
                .build();

        notiProducer.sendNotiCreatePotentialCustomer(notificationEvent);
    }

    @Override
    @Transactional
    public void markAllNotificationsAsRead(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUser_UserIdAndStatus(userId, false);
        notifications.forEach(notification -> notification.setStatus(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    @SneakyThrows
    public void markNotificationAsRead(UUID userId, Long notificationId) {
        Notification notification = notificationRepository.findByNotificationIdAndUser_UserId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setStatus(true);
        notificationRepository.save(notification);
    }


    @Override
    public void deleteNotification(List<Long> notificationIds) {
        notificationRepository.deleteAllById(notificationIds);
    }

    @Override
    public List<NotificationDto> getNotifications(UUID userId) {
        return notificationMapper.toDtoList(notificationRepository.findByUser_UserId(userId).stream()
                .sorted(Comparator.comparing(Notification::isStatus).reversed()
                        .thenComparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList()));
    }
}
