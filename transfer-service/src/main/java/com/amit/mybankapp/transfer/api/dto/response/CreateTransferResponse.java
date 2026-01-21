package com.amit.mybankapp.transfer.api.dto.response;

import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;

import java.util.UUID;

public record CreateTransferResponse(
        UUID transferId,
        TransferStatus status) {
}
