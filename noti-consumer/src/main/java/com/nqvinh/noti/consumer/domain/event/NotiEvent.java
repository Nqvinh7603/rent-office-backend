package com.nqvinh.noti.consumer.domain.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
