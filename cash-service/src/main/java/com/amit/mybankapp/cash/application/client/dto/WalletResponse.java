package com.amit.mybankapp.cash.application.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        UUID customerId,
        BigDecimal balance) {
}
