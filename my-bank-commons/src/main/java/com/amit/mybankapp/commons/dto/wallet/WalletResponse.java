package com.amit.mybankapp.commons.dto.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        UUID customerId,
        BigDecimal balance) {
}
