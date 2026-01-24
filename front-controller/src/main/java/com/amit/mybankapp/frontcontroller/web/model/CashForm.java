package com.amit.mybankapp.frontcontroller.web.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CashForm(
        @NotNull(message = "amount must not be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "amount must be greater than 0")
        BigDecimal amount) {

    public static CashForm empty() {
        return new CashForm(null);
    }

}
