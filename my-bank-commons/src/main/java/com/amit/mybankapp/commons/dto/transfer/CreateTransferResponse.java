package com.amit.mybankapp.commons.dto.transfer;

import java.util.UUID;

public record CreateTransferResponse(
        UUID transferId,
        String status) {
}