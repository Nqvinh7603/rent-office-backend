package com.nqvinh.rentofficebackend.domain.common.repository;

import com.nqvinh.rentofficebackend.domain.common.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserIdAndStatus(UUID userId, boolean status);
    Optional<Notification> findByNotificationIdAndUser_UserId(Long notificationId, UUID userId);
    List<Notification> findByUser_UserId(UUID userId);
}
