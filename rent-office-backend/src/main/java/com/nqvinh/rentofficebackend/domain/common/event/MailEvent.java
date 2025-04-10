package com.nqvinh.rentofficebackend.domain.common.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
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


