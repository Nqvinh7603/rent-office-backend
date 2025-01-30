package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.MailProducer;
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
public class MailProducerImpl implements MailProducer {

    @Value("${kafka.email}")
    String mailTopic;

    @Value("${spring.application.name}")
    String serviceId;

    final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(MailEvent mail) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    MessageCode.MAIL_RESET_PASSWORD.getCode(),
                    mail
            );
            kafkaTemplate.send(mailTopic, message);
            log.info("Produced a message to topic: {}, value: {}", mailTopic, mail);
        } catch (Exception e) {
           log.error("Failed to produce the message to topic: {}", mailTopic, e);
        }
    }
}
