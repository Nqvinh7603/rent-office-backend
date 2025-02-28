package com.nqvinh.noti.consumer.application.kafka;

import com.nqvinh.noti.consumer.application.dto.NotiMessageKafka;
import com.nqvinh.noti.consumer.domain.service.NotiWebSocketHandler;
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
public class KafkaNotiConsumer {

    NotiWebSocketHandler notiWebSocketHandler;

    @RetryableTopic(
            attempts = "3", // 1 primary topic + 3 retry topics + 1 DLT topic
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "false",
            include = {RetriableException.class, RuntimeException.class}
    )
    @KafkaListener(
            topics = "${kafka.noti.topic}",
            properties = {"spring.json.value.default.type=com.nqvinh.noti.consumer.application.dto.NotiMessageKafka"},
            concurrency = "${kafka.noti.concurrency}"
    )
    @SneakyThrows
    public void listenNotis(@Payload NotiMessageKafka message) {
        log.info("Received a transaction message - code: {} - meta: {} - payload: {}", message.getMessageCode(), message.getMeta(), message.getPayload());
        notiWebSocketHandler.sendNotification(message.getPayload());
    }

    @DltHandler
    public void retryNotis(@Payload NotiMessageKafka message) {
        log.warn("Retrying transaction message: {}", message);
        try {
            notiWebSocketHandler.sendNotification(message.getPayload());
        } catch (Exception e) {
            log.error("Failed to process retried transaction message: {}", message, e);
        }
    }
}
