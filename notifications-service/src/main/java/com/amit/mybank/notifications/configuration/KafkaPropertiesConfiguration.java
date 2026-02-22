package com.amit.mybank.notifications.configuration;

import com.amit.mybank.notifications.configuration.properties.KafkaErrorHandlingProperties;
import com.amit.mybank.notifications.configuration.properties.KafkaTopicsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        KafkaTopicsProperties.class,
        KafkaErrorHandlingProperties.class
})
public class KafkaPropertiesConfiguration {
}
