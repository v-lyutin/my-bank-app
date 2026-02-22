package com.amit.mybank.notifications.configuration;

import com.amit.mybank.notifications.configuration.properties.KafkaErrorHandlingProperties;
import com.amit.mybank.notifications.configuration.properties.KafkaTopicsProperties;
import com.amit.mybank.notifications.messaging.dlq.DeadLetterQueueHeaderProvider;
import com.amit.mybank.notifications.messaging.dlq.DeadLetterQueueTopicResolver;
import jakarta.validation.ConstraintViolationException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.converter.ConversionException;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import java.util.Map;
import java.util.function.BiFunction;

@Configuration
public class KafkaErrorHandlingConfiguration {

    private static final int MAX_EXCEPTION_MESSAGE_BYTES = 2 * 1024;

    private static final int DEAD_LETTER_QUEUE_PARTITION = 0;

    @Bean
    public DeadLetterQueueTopicResolver deadLetterQueueTopicResolver(KafkaTopicsProperties kafkaTopicsProperties,
                                                                     KafkaErrorHandlingProperties kafkaErrorHandlingProperties) {
        Map<String, String> routing = Map.of(
                kafkaTopicsProperties.walletOperationCompleted(), kafkaTopicsProperties.walletOperationCompletedDlq(),
                kafkaTopicsProperties.transferCreated(), kafkaTopicsProperties.transferCreatedDlq()
        );
        return new DeadLetterQueueTopicResolver(routing, kafkaErrorHandlingProperties.dlqSuffix());
    }

    @Bean
    public DeadLetterQueueHeaderProvider deadLetterQueueHeaderProvider() {
        return new DeadLetterQueueHeaderProvider(MAX_EXCEPTION_MESSAGE_BYTES);
    }

    @Bean
    public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<byte[], byte[]> deadLetterQueueKafkaTemplate,
                                                                       DeadLetterQueueTopicResolver deadLetterQueueTopicResolver,
                                                                       DeadLetterQueueHeaderProvider deadLetterQueueHeaderProvider) {
        BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> destinationResolver =
                (record, exception) -> new TopicPartition(deadLetterQueueTopicResolver.resolve(record.topic()), DEAD_LETTER_QUEUE_PARTITION);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(deadLetterQueueKafkaTemplate, destinationResolver);
        recoverer.setHeadersFunction(deadLetterQueueHeaderProvider::build);
        return recoverer;
    }

    @Bean
    public DefaultErrorHandler defaultErrorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer,
                                                   KafkaErrorHandlingProperties kafkaErrorHandlingProperties) {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(kafkaErrorHandlingProperties.maxRetries());
        backOff.setInitialInterval(kafkaErrorHandlingProperties.initialIntervalMs());
        backOff.setMultiplier(kafkaErrorHandlingProperties.multiplier());
        backOff.setMaxInterval(kafkaErrorHandlingProperties.maxIntervalMs());

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(deadLetterPublishingRecoverer, backOff);

        errorHandler.addNotRetryableExceptions(
                DeserializationException.class,
                SerializationException.class,
                ConversionException.class,
                MethodArgumentNotValidException.class,
                ConstraintViolationException.class,
                IllegalArgumentException.class
        );

        return errorHandler;
    }

}
