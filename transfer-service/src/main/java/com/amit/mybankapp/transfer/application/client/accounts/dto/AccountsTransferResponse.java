package com.amit.mybankapp.transfer.application.client.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountsTransferResponse(
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount) {
}
