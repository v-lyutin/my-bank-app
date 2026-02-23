package com.amit.mybank.notifications.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybank.kafka.topics")
public record KafkaTopicsProperties(
        String walletOperationCompleted,
        String transferCreated,
        String walletOperationCompletedDlq,
        String transferCreatedDlq) {
}
