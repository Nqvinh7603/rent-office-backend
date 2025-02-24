package com.nqvinh.mail.consumer.domain.service;

import com.nqvinh.mail.consumer.domain.event.MailEvent;

public interface MailEngine {
    void sendMail(MailEvent mail);
}
