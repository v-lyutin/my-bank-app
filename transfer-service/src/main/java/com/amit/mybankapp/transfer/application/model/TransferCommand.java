package com.amit.mybankapp.transfer.application.model;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferCommand(
        UUID recipientCustomerId,
        BigDecimal amount) {
}
