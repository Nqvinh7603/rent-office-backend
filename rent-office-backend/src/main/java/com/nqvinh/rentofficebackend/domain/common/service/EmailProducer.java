package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;

public interface EmailProducer {
    void sendMailResetPassword(MailEvent mail);
    void sendMailCancelledConsignment(MailEvent mail);
    void sendMailNewConsignment(MailEvent mail);
    void sendMailIncompleteConsignment(MailEvent mail);
}
