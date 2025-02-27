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
    MAIL_INCOMPLETE_CONSIGNMENT("MAIL_INCOMPLETE_CONSIGNMENT"),
    NOTIFICATION_CREATE_CONSIGNMENT("NOTIFICATION_CREATE_CONSIGNMENT"),
    NOTIFICATION_UPDATE_INFO_CONSIGNMENT("NOTIFICATOIN_UPDATE_INFO_CONSIGNMENT"),
    NOTIFICATION_ASSIGN_CUSTOMER("NOTIFICATION_ASSIGN_CUSTOMER");
    String code;
}
