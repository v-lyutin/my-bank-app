package com.amit.mybankapp.cash.application.messaging;

import com.amit.mybankapp.cash.application.messaging.event.WalletOperationCompletedEvent;
import com.amit.mybankapp.cash.application.messaging.exception.NotificationPublishException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class NotificationsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsProducer.class);

    private static final String TYPE_ID_WALLET_OPERATION_COMPLETED_V1 = "wallet-operation-completed.v1";

    private static final String HEADER_TYPE_ID = "__TypeId__";

    private static final String HEADER_OPERATION_ID = "operationId";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String topic;

    private final MeterRegistry meterRegistry;

    @Autowired
    public NotificationsProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value(value = "${mybank.kafka.topics.walletOperationCompleted}") String topic,
                                 MeterRegistry meterRegistry) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.meterRegistry = meterRegistry;
    }

    public CompletableFuture<Void> send(WalletOperationCompletedEvent event) {
        String operationId = event.operationId().toString();
        ProducerRecord<String, Object> record = buildRecord(event, operationId);

        return this.kafkaTemplate.send(record)
                .thenAccept(result -> {
                    RecordMetadata recordMetadata = result.getRecordMetadata();
                    LOGGER.info("Published {}: topic={}, partition={}, offset={}, operationId={}",
                            TYPE_ID_WALLET_OPERATION_COMPLETED_V1,
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
                            operationId);
                })
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        this.recordNotificationError(event.customerId());
                        Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

                        LOGGER.warn("Failed publishing {} (topic={}, operationId={})",
                                TYPE_ID_WALLET_OPERATION_COMPLETED_V1,
                                topic,
                                operationId,
                                cause);

                        throw new NotificationPublishException(
                                "Failed publishing " + TYPE_ID_WALLET_OPERATION_COMPLETED_V1 + " (topic=" + topic + ", operationId=" + operationId + ")",
                                cause
                        );
                    }
                });
    }

    private ProducerRecord<String, Object> buildRecord(WalletOperationCompletedEvent event, String operationId) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(this.topic, operationId, event);

        producerRecord.headers().add(HEADER_TYPE_ID, TYPE_ID_WALLET_OPERATION_COMPLETED_V1.getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add(HEADER_OPERATION_ID, operationId.getBytes(StandardCharsets.UTF_8));

        return producerRecord;
    }

    private void recordNotificationError(UUID customerId) {
        Counter.builder("mybank.notification.failed")
                .description("Total number of failed notification deliveries to Kafka")
                .tag("customer_id", customerId.toString())
                .tag("service", "cash-service")
                .register(this.meterRegistry)
                .increment();
    }

}
