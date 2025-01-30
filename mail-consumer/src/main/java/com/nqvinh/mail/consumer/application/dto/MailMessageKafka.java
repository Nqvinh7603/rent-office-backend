package com.nqvinh.mail.consumer.application.dto;

import com.nqvinh.mail.consumer.domain.event.MailEvent;
import dev.nqvinh.kafka.common.kafka_lib.dto.KafkaMessage;

public class MailMessageKafka extends KafkaMessage<MailEvent> {
}
