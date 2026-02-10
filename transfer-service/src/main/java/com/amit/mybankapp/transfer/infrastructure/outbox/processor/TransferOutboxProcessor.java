package com.amit.mybankapp.transfer.infrastructure.outbox.processor;

import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.TransferOutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class TransferOutboxProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferOutboxProcessor.class);

    private static final int DEFAULT_BATCH_SIZE = 50;

    private final TransferOutboxRepository transferOutboxRepository;

    private final TransferOutboxAcquirer transferOutboxAcquirer;

    private final NotificationsClient notificationsClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public TransferOutboxProcessor(TransferOutboxRepository transferOutboxRepository,
                                   TransferOutboxAcquirer transferOutboxAcquirer,
                                   NotificationsClient notificationsClient,
                                   ObjectMapper objectMapper) {
        this.transferOutboxRepository = transferOutboxRepository;
        this.transferOutboxAcquirer = transferOutboxAcquirer;
        this.notificationsClient = notificationsClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelayString = "${transfers.outbox.dispatcher.delay-ms:1000}")
    public void processTransferOutboxRecords() {
        List<TransferOutboxRecord> batch = this.transferOutboxAcquirer.acquirePendingEvents(DEFAULT_BATCH_SIZE);
        batch.forEach(this::processTransferOutboxRecord);
    }

    private void processTransferOutboxRecord(TransferOutboxRecord transferOutboxRecord) {
        UUID outboxId = transferOutboxRecord.id();

        try {
            if (transferOutboxRecord.eventType() == TransferOutboxRecord.TransferEventType.TRANSFER_CREATED) {
                TransferCreatedEvent transferCreatedEvent = this.objectMapper.readValue(transferOutboxRecord.payload(), TransferCreatedEvent.class);
                this.notificationsClient.sendTransferCreatedEvent(transferCreatedEvent);
            } else {
                LOGGER.warn("Unknown outbox event type: outboxId={}, eventType={}", outboxId, transferOutboxRecord.eventType());
                this.transferOutboxRepository.markAsFailed(outboxId);
                return;
            }

            boolean updatedRows = this.transferOutboxRepository.markAsSent(outboxId, Instant.now());
            if (!updatedRows) {
                LOGGER.warn("Outbox event was not marked as SENT (status changed?): outboxId={}", outboxId);
            }

        } catch (Exception exception) {
            boolean updatedRows = this.transferOutboxRepository.markAsFailed(outboxId);
            if (!updatedRows) {
                LOGGER.warn("Outbox event was not marked as FAILED (status changed?): outboxId={}", outboxId);
            }

            LOGGER.error(
                    "Failed to dispatch outbox event: outboxId={}, transferId={}, eventType={}",
                    outboxId,
                    transferOutboxRecord.transferId(),
                    transferOutboxRecord.eventType(),
                    exception
            );
        }
    }

}
