package com.amit.mybankapp.commons.dto.wallet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WalletOperationRequest(
        @NotNull(message = "amount must not be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "amount must be greater than 0")
        BigDecimal amount) {
}
