package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.NotiEvent;
import com.nqvinh.rentofficebackend.domain.common.service.NotiProducer;
import dev.nqvinh.kafka.common.kafka_lib.constant.EventType;
import dev.nqvinh.kafka.common.kafka_lib.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotiProducerImpl implements NotiProducer {

    final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.noti}")
    String notiTopic;

    @Value("${spring.application.name}")
    String serviceId;

    @Override
    public void sendNotiCreateConsignment(NotiEvent noti) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.NOTIFICATION_CREATE_BUILDING.getCode(),
                    noti
            );
            kafkaTemplate.send(notiTopic, message);
            log.info("Produced a message to topic: {}, value: {}", notiTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", notiTopic, e);
        }
    }

    @Override
    public void sendNotiUpdateConsignment(NotiEvent noti) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.NOTIFICATION_UPDATE_INFO_BUILDING.getCode(),
                    noti
            );
            kafkaTemplate.send(notiTopic, message);
            log.info("Produced a message to topic: {}, value: {}", notiTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", notiTopic, e);
        }
    }

    @Override
    public void sendNotiAssignCustomer(NotiEvent noti) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.NOTIFICATION_ASSIGN_CUSTOMER.getCode(),
                    noti
            );
            kafkaTemplate.send(notiTopic, message);
            log.info("Produced a message to topic: {}, value: {}", notiTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", notiTopic, e);
        }
    }

    @Override
    public void sendNotiCreatePotentialCustomer(NotiEvent noti) {
        try{
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.NOTIFICATION_CREATE_CUSTOMER_POTENTIAL.getCode(),
                    noti
            );
            kafkaTemplate.send(notiTopic, message);
            log.info("Produced a message to topic: {}, value: {}", notiTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", notiTopic, e);
        }
    }

    @Override
    public void sendNotiCreateAppointment(NotiEvent noti) {
        try{
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.NOTIFICATION_CREATE_CUSTOMER_POTENTIAL.getCode(),
                    noti
            );
            kafkaTemplate.send(notiTopic, message);
            log.info("Produced a message to topic: {}, value: {}", notiTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", notiTopic, e);
        }
    }

}
