package com.amit.mybankapp.accounts.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        UUID customerId,
        BigDecimal balance) {
}
