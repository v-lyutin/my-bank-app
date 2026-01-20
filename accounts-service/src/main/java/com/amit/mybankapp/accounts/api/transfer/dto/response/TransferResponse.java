package com.amit.mybankapp.accounts.api.transfer.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount) {
}

