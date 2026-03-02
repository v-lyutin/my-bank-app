package com.amit.mybankapp.transfer.infrastructure.outbox.producer;

import com.amit.mybankapp.transfer.infrastructure.outbox.producer.event.TransferCreatedEvent;
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
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class NotificationsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsProducer.class);

    private static final String TYPE_ID_TRANSFER_CREATED_V1 = "transfer-created.v1";

    private static final String HEADER_TYPE_ID = "__TypeId__";

    private static final String HEADER_OUTBOX_ID = "outboxId";

    private static final String HEADER_TRANSFER_ID = "transferId";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String transferCreatedTopic;

    @Autowired
    public NotificationsProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                 @Value("${mybank.kafka.topics.transferCreated}") String transferCreatedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.transferCreatedTopic = transferCreatedTopic;
    }

    public void sendTransferCreated(TransferCreatedEvent event, UUID outboxId) {
        String transferId = event.transferId().toString();
        ProducerRecord<String, Object> producerRecord = this.buildRecord(event, outboxId, transferId);

        try {
            SendResult<String, Object> sendResult = this.kafkaTemplate.send(producerRecord).get();
            RecordMetadata recordMetadata = sendResult.getRecordMetadata();

            LOGGER.info("Published {}: topic={}, partition={}, offset={}, outboxId={}, transferId={}",
                    TYPE_ID_TRANSFER_CREATED_V1,
                    recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset(),
                    outboxId, transferId
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new NotificationPublishException(
                    "Interrupted while publishing " + TYPE_ID_TRANSFER_CREATED_V1 + " (outboxId=" + outboxId + ", transferId=" + transferId + ")",
                    exception
            );
        } catch (ExecutionException executionException) {
            Throwable cause = executionException.getCause() != null ? executionException.getCause() : executionException;
            throw new NotificationPublishException(
                    "Failed publishing " + TYPE_ID_TRANSFER_CREATED_V1 + " (topic=" + transferCreatedTopic + ", outboxId=" + outboxId + ", transferId=" + transferId + ")",
                    cause
            );
        }
    }

    private ProducerRecord<String, Object> buildRecord(TransferCreatedEvent event, UUID outboxId, String transferId) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(transferCreatedTopic, transferId, event);

        producerRecord.headers().add(HEADER_TYPE_ID, TYPE_ID_TRANSFER_CREATED_V1.getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add(HEADER_OUTBOX_ID, outboxId.toString().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add(HEADER_TRANSFER_ID, transferId.getBytes(StandardCharsets.UTF_8));

        return producerRecord;
    }

    public static class NotificationPublishException extends RuntimeException {
        public NotificationPublishException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
