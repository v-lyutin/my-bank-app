package com.amit.mybankapp.commons.client.dto.transfer;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferResponse(
        UUID transferId,
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount,
        String status) {
}