package com.amit.mybank.notifications.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybank.kafka.error-handling")
public record KafkaErrorHandlingProperties(
        int maxRetries,
        long initialIntervalMs,
        double multiplier,
        long maxIntervalMs,
        String dlqSuffix) {
}
