package com.amit.mybank.notifications.messaging.event;

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
}
