package com.amit.mybankapp.accounts.application.wallet.model;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationResult(
        UUID operationId,
        String operationName,
        UUID walletId,
        UUID customerId,
        BigDecimal amount) {
}
