package com.amit.mybankapp.transfer.application.client.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountsTransferRequest(
        UUID recipientCustomerId,
        BigDecimal amount) {
}
