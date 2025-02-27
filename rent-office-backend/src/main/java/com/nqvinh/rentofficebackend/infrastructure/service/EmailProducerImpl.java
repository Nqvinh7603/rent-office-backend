package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
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
public class EmailProducerImpl implements EmailProducer {
    final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.email}")
    String emailTopic;

    @Value("${spring.application.name}")
    String serviceId;

    @Override
    public void sendMailResetPassword(MailEvent mail) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.MAIL_RESET_PASSWORD.getCode(),
                    mail
            );
            kafkaTemplate.send(emailTopic, message);
            log.info("Produced a message to topic: {}, value: {}", emailTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", emailTopic, e);
        }
    }

    @Override
    public void sendMailCancelledConsignment(MailEvent mail) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.MAIL_CANCELLED_CONSIGNMENT.getCode(),
                    mail
            );
            kafkaTemplate.send(emailTopic, message);
            log.info("Produced a message to topic: {}, value: {}", emailTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", emailTopic, e);
        }
    }

    @Override
    public void sendMailNewConsignment(MailEvent mail) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.MAIL_PENDING_CONSIGNMENT.getCode(),
                    mail
            );
            kafkaTemplate.send(emailTopic, message);
            log.info("Produced a message to topic: {}, value: {}", emailTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", emailTopic, e);
        }
    }

    @Override
    public void sendMailIncompleteConsignment(MailEvent mail) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.MAIL_INCOMPLETE_CONSIGNMENT.getCode(),
                    mail
            );
            kafkaTemplate.send(emailTopic, message);
            log.info("Produced a message to topic: {}, value: {}", emailTopic, message);
        } catch (Exception e) {
            log.error("Failed to produce the message to topic: {}", emailTopic, e);
        }
    }

}
