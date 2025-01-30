package com.nqvinh.mail.consumer.domain.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailEvent {
    String toAddress;
    String subject;
    String templateName;
    Map<String, Object> context;
    String type;
    String code;
    int status;
    String message;
}
