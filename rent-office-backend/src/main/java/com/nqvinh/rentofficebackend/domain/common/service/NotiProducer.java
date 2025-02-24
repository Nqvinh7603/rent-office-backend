package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.common.event.NotiEvent;

public interface NotiProducer {

    void sendNotiCreateConsignment(NotiEvent noti);
}
