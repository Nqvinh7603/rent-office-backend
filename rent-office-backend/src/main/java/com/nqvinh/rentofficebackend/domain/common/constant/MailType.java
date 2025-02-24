package com.nqvinh.rentofficebackend.domain.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum MailType {
    RESET_PASSWORD("RESET_PASSWORD"),
    CONFIRMED_CONSIGNMENT("CONFIRMED_CONSIGNMENT"),
    CANCELLED_CONSIGNMENT("CANCELLED_CONSIGNMENT"),
    PENDING_CONSIGNMENT("PENDING_CONSIGNMENT"),
    NOTIFICATION_CREATE_CONSIGNMENT("NOTIFICATION_CREATE_CONSIGNMENT");
    String type;
}
