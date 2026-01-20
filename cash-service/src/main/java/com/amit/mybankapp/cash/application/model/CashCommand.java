package com.amit.mybankapp.cash.application.model;

import com.amit.mybankapp.cash.application.model.exception.InvalidCashCommandException;
import com.amit.mybankapp.cash.application.model.type.CashCommandType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public record CashCommand(
        CashCommandType type,
        UUID walletId,
        BigDecimal amount) {

    public CashCommand {

        if (type == null || walletId == null || amount == null) {
            throw new InvalidCashCommandException("walletId/type/amount must not be null");
        }

        if (amount.signum() <= 0) {
            throw new InvalidCashCommandException("amount must be > 0");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

}
