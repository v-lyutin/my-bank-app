package com.amit.mybank.notifications.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaDeadLetterQueueProducerConfiguration {

    @Bean
    public ProducerFactory<byte[], byte[]> deadLetterQueueProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> producerProperties = new HashMap<>(kafkaProperties.buildProducerProperties());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public KafkaTemplate<byte[], byte[]> deadLetterQueueKafkaTemplate(ProducerFactory<byte[], byte[]> dlqProducerFactory) {
        return new KafkaTemplate<>(dlqProducerFactory);
    }

}
