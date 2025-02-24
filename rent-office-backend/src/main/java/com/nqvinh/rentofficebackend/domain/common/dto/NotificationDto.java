package com.nqvinh.rentofficebackend.domain.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDto {
    Long notificationId;
    Long consignmentId;
    String message;
    boolean status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
