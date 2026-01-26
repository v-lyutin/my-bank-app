package com.amit.mybankapp.cash.application.model;

import com.amit.mybankapp.cash.application.model.exception.InvalidCashCommandException;
import com.amit.mybankapp.cash.application.model.type.WalletCommandType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CashCommand(
        WalletCommandType type,
        BigDecimal amount) {

    public CashCommand {

        if (type == null || amount == null) {
            throw new InvalidCashCommandException("operationType/amount must not be null");
        }

        if (amount.signum() <= 0) {
            throw new InvalidCashCommandException("amount must be > 0");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

}
