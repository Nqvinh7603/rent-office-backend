package com.nqvinh.rentofficebackend.domain.common.constant;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum MailStatus {

    INIT(0),
    SUCCESSFUL(1),
    FAILED(-1);

    int status;
}
