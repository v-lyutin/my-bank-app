package com.amit.mybankapp.cash.application.model;

import com.amit.mybankapp.cash.application.model.exception.InvalidCashCommandException;
import com.amit.mybankapp.commons.model.type.WalletOperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record WalletOperationCommand(
        WalletOperationType walletOperationType,
        BigDecimal amount) {

    public WalletOperationCommand {

        if (walletOperationType == null || amount == null) {
            throw new InvalidCashCommandException("walletOperationType/amount must not be null");
        }

        if (amount.signum() <= 0) {
            throw new InvalidCashCommandException("amount must be > 0");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

}
