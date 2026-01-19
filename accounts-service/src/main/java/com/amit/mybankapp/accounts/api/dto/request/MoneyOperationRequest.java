package com.amit.mybankapp.accounts.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MoneyOperationRequest(
        @NotNull(message = "amount must not be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "amount must be greater than 0")
        BigDecimal amount) {
}
