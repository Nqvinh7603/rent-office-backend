package com.nqvinh.rentofficebackend.domain.common.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotiEvent {
    Long notificationId;
    String message;
    boolean status;
    List<UUID> userId;
    Long consignmentId;
    LocalDateTime createdAt;
    String type;
    String code;
}
