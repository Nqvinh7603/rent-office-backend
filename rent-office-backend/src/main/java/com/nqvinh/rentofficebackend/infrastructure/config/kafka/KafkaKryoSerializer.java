package com.nqvinh.rentofficebackend.infrastructure.config.kafka;

import com.nqvinh.rentofficebackend.infrastructure.config.kryo.KryoSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaKryoSerializer implements Serializer<Object> {

    @Autowired
    private KryoSerializer kryoSerializer;

    @Override
    public byte[] serialize(String topic, Object data) {
        return kryoSerializer.serialize(data);
    }
}
