package com.nqvinh.noti.consumer.application.dto;

import com.nqvinh.noti.consumer.domain.event.MailEvent;
import dev.nqvinh.kafka.common.kafka_lib.dto.KafkaMessage;


public class MailMessageKafka extends KafkaMessage<MailEvent> {
}
