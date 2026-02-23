package com.amit.mybankapp.cash.application.messaging;

import com.amit.mybankapp.cash.application.messaging.event.WalletOperationCompletedEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

@Component
public class NotificationsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsProducer.class);

    private static final String TYPE_ID_WALLET_OPERATION_COMPLETED_V1 = "wallet-operation-completed.v1";

    private static final String HEADER_TYPE_ID = "__TypeId__";

    private static final String HEADER_OPERATION_ID = "operationId";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String topic;

    @Autowired
    public NotificationsProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value(value = "${mybank.kafka.topics.walletOperationCompleted}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void send(WalletOperationCompletedEvent event) {
        String operationId = event.operationId().toString();
        ProducerRecord<String, Object> producerRecord = this.buildRecord(event, operationId);

        try {
            SendResult<String, Object> sendResult = this.kafkaTemplate.send(producerRecord).get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();

            LOGGER.info("Published {}: topic={}, partition={}, offset={}, operationId={}",
                    TYPE_ID_WALLET_OPERATION_COMPLETED_V1,
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
                    operationId
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new NotificationPublishException(
                    "Interrupted while publishing " + TYPE_ID_WALLET_OPERATION_COMPLETED_V1 + " (operationId=" + operationId + ")",
                    exception
            );
        } catch (ExecutionException executionException) {
            Throwable cause = executionException.getCause() != null ? executionException.getCause() : executionException;
            throw new NotificationPublishException(
                    "Failed publishing " + TYPE_ID_WALLET_OPERATION_COMPLETED_V1 + " (topic=" + this.topic + ", operationId=" + operationId + ")",
                    cause
            );
        }
    }

    private ProducerRecord<String, Object> buildRecord(WalletOperationCompletedEvent event, String operationId) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(this.topic, operationId, event);

        producerRecord.headers().add(HEADER_TYPE_ID, TYPE_ID_WALLET_OPERATION_COMPLETED_V1.getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add(HEADER_OPERATION_ID, operationId.getBytes(StandardCharsets.UTF_8));

        return producerRecord;
    }

    public static class NotificationPublishException extends RuntimeException {
        public NotificationPublishException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
