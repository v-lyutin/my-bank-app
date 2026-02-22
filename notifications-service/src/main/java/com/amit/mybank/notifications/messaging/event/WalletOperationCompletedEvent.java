package com.amit.mybank.notifications.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record WalletOperationCompletedEvent(
        UUID eventId,
        Instant occurredAt,
        UUID operationId,
        String operationName,
        UUID walletId,
        UUID customerId,
        BigDecimal amount) {
}
