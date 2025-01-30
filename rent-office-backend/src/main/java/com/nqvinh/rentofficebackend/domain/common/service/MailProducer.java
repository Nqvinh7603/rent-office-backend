package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;

public interface MailProducer {
    void send(MailEvent mail);
}
