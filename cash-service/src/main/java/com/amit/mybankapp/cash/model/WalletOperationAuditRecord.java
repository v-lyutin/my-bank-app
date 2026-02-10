package com.amit.mybankapp.cash.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record WalletOperationAuditRecord(
        UUID operationId,
        String operationType,
        UUID walletId,
        UUID customerId,
        BigDecimal amount,
        String status,
        Instant createdAt) {
}
