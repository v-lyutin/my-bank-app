package com.amit.mybankapp.commons.model.event;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferCreatedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID transferId,
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount,
        String status) {

    public static TransferCreatedEvent from(CreateTransferResponse createTransferResponse) {
        return new TransferCreatedEvent(
                UUID.randomUUID(),
                Instant.now(),
                createTransferResponse.transferId(),
                createTransferResponse.senderCustomerId(),
                createTransferResponse.recipientCustomerId(),
                createTransferResponse.amount(),
                createTransferResponse.status()
        );
    }

}
