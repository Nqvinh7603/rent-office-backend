package com.nqvinh.noti.consumer.application.dto;

import com.nqvinh.noti.consumer.domain.event.NotiEvent;
import dev.nqvinh.kafka.common.kafka_lib.dto.KafkaMessage;

public class NotiMessageKafka extends KafkaMessage<NotiEvent> {
}
