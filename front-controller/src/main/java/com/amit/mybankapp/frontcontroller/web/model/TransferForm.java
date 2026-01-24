package com.amit.mybankapp.frontcontroller.web.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferForm(
        @NotNull(message = "recipientCustomerId must not be null")
        UUID recipientCustomerId,

        @NotNull(message = "amount must not be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "amount must be greater than 0")
        BigDecimal amount) {

    public static TransferForm empty() {
        return new TransferForm(null, null);
    }

}
