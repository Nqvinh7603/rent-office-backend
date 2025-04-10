package com.nqvinh.noti.consumer.domain.service;

import com.nqvinh.noti.consumer.domain.event.MailEvent;

public interface MailEngine {
    void sendMail(MailEvent mail);
}
