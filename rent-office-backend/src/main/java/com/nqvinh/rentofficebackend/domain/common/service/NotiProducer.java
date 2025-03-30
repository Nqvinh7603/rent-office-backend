package com.nqvinh.rentofficebackend.domain.common.service;

import com.nqvinh.rentofficebackend.domain.common.event.NotiEvent;

public interface NotiProducer {
    void sendNotiCreateConsignment(NotiEvent noti);
    void sendNotiUpdateConsignment(NotiEvent noti);
    void sendNotiAssignCustomer(NotiEvent noti);
    void sendNotiCreatePotentialCustomer(NotiEvent noti);
    void sendNotiCreateAppointment(NotiEvent noti);
}
