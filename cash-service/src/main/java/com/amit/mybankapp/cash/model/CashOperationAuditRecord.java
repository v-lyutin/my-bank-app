package com.amit.mybankapp.cash.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CashOperationAuditRecord(
        UUID operationId,
        UUID walletId,
        String type,
        BigDecimal amount,
        String status,
        Instant createdAt) {
}
