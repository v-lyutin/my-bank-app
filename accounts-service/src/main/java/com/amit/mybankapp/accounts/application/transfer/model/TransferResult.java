package com.amit.mybankapp.accounts.application.transfer.model;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResult(
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount) {
}
