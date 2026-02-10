package com.amit.mybankapp.transfer.infrastructure.outbox.model;

import java.time.Instant;
import java.util.UUID;

public record TransferOutboxRecord(
        UUID id,
        UUID transferId,
        TransferEventType eventType,
        String payload,
        TransferOutboxStatus status,
        Instant createdAt,
        Instant processingStartedAt,
        Instant processedAt) {

    public static TransferOutboxRecord pending(UUID id, UUID transferId, TransferEventType eventType, String payload, Instant createdAt) {
        return new TransferOutboxRecord(
                id,
                transferId,
                eventType,
                payload,
                TransferOutboxStatus.PENDING,
                createdAt,
                null,
                null
        );
    }

    public enum TransferEventType {

        TRANSFER_CREATED

    }

    public enum TransferOutboxStatus {

        PENDING,

        PROCESSING,

        SENT,

        FAILED

    }

}
