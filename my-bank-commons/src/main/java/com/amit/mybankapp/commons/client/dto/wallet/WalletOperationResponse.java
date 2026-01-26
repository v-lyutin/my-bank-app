package com.amit.mybankapp.commons.client.dto.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationResponse(
        UUID operationId,
        String operationName,
        UUID walletId,
        UUID customerId,
        BigDecimal amount,
        String status) {
}
