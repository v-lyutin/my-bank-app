package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.cash.application.model.type.CashCommandType;

import java.math.BigDecimal;

public interface CashCommandProcessor {

    CashCommandType type();

    void process(BigDecimal amount);

}
