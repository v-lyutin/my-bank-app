package com.amit.mybankapp.transfer.application.model;

import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;

import java.util.UUID;

public record TransferResult(
        UUID transferId,
        TransferStatus status) {
}
