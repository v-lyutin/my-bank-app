package com.amit.mybankapp.frontcontroller.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferCommand(
        UUID recipientCustomerId,
        BigDecimal amount) {
}
