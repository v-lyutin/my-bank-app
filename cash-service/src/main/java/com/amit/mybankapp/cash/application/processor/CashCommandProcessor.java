package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.cash.application.model.type.CashCommandType;

import java.math.BigDecimal;
import java.util.UUID;

public interface CashCommandProcessor {

    CashCommandType type();

    void process(UUID walletId, BigDecimal amount);

}
