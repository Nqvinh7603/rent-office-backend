package com.nqvinh.rentofficebackend.application.api.notification;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.common.dto.NotificationDto;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(UrlConstant.NOTIFICATIONS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NotificationController {
    NotificationService notificationService;

    @PutMapping(UrlConstant.MARK_ALL_NOTIFICATIONS_AS_READ)
    public ApiResponse<Void> markAllNotificationsAsRead( @RequestParam UUID userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Notifications"))
                .build();
    }

    @PutMapping(UrlConstant.MARK_NOTIFICATION_AS_READ)
    public ApiResponse<Void> markNotificationAsRead(@RequestParam UUID userId, @PathVariable("id") Long notificationId) {
        notificationService.markNotificationAsRead(userId, notificationId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Notification"))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteNotification(@RequestBody List<Long> notifications) {
        notificationService.deleteNotification(notifications);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Notification"))
                .build();
    }

    @GetMapping(UrlConstant.GET_NOTIFICATIONS_BY_USER_ID)
    public ApiResponse<List<NotificationDto>> getNotificationsByUserId(@PathVariable("id") UUID userId) {
        return ApiResponse.<List<NotificationDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Notifications"))
                .payload(notificationService.getNotifications(userId))
                .build();
    }

}
