package com.nqvinh.mail.consumer.application.kafka;

import com.nqvinh.mail.consumer.application.dto.MailMessageKafka;
import com.nqvinh.mail.consumer.domain.service.MailEngine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class KafkaConsumer {

    MailEngine mailEngine;

    @RetryableTopic(
            attempts = "3", // 1 primary topic + 3 retry topics + 1 DLT topic
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "false",
            include = {RetriableException.class, RuntimeException.class}
    )
    @KafkaListener(
            topics = "${kafka.email.topic}",
            properties = {"spring.json.value.default.type=com.nqvinh.mail.consumer.application.dto.MailMessageKafka"},
            concurrency = "${kafka.email.concurrency}"
    )
    @SneakyThrows
    public void listenMails(@Payload MailMessageKafka message) {
        log.info("Received a transaction message - code: {} - meta: {} - payload: {}", message.getMessageCode(), message.getMeta(), message.getPayload());
        mailEngine.sendMail(message.getPayload());
    }

    @DltHandler
    public void retryMails(@Payload MailMessageKafka message) {
        log.warn("Retrying transaction message: {}", message);
        try {
            mailEngine.sendMail(message.getPayload());
        } catch (Exception e) {
            log.error("Failed to process retried transaction message: {}", message, e);
        }
    }
}
