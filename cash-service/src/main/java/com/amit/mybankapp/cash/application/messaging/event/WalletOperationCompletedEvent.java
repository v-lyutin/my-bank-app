package com.amit.mybankapp.cash.application.messaging.event;

import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;

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

    public static WalletOperationCompletedEvent from(WalletOperationResponse walletOperationResponse) {
        return new WalletOperationCompletedEvent(
                UUID.randomUUID(),
                Instant.now(),
                walletOperationResponse.operationId(),
                walletOperationResponse.operationName(),
                walletOperationResponse.walletId(),
                walletOperationResponse.customerId(),
                walletOperationResponse.amount()
        );
    }

}
