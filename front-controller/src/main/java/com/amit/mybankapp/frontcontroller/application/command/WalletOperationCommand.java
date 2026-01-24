package com.amit.mybankapp.frontcontroller.application.command;

import java.math.BigDecimal;

public record WalletOperationCommand(
        BigDecimal amount) {
}

