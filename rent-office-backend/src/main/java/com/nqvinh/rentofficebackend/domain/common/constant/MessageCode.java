package com.nqvinh.rentofficebackend.domain.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum MessageCode {
    MAIL_RESET_PASSWORD("MAIL_RESET_PASSWORD"),
    MAIL_CANCELLED_CONSIGNMENT("MAIL_CANCELLED_CONSIGNMENT"),
    MAIL_PENDING_CONSIGNMENT("MAIL_PENDING_CONSIGNMENT"),
    NOTIFICATION_CREATE_CONSIGNMENT("NOTIFICATION_CREATE_CONSIGNMENT");
    String code;
}
