package com.nqvinh.rentofficebackend.domain.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum MailType {
    RESET_PASSWORD("RESET_PASSWORD");
    String type;
}
