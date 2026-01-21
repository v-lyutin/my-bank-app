package com.amit.mybankapp.transfer.infrastructure.audit.model;

import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferAuditRecord(
        UUID transferId,
        UUID senderCustomerId,
        UUID recipientCustomerId,
        BigDecimal amount,
        TransferStatus status,
        Instant createdAt) {

    public static TransferAuditRecord accepted(UUID transferId, UUID senderCustomerId, UUID recipientCustomerId, BigDecimal amount) {
        return new TransferAuditRecord(
                transferId,
                senderCustomerId,
                recipientCustomerId,
                amount,
                TransferStatus.ACCEPTED,
                Instant.now()
        );
    }

    public static TransferAuditRecord rejected(UUID transferId, UUID senderCustomerId, UUID recipientCustomerId, BigDecimal amount) {
        return new TransferAuditRecord(
                transferId,
                senderCustomerId,
                recipientCustomerId,
                amount,
                TransferStatus.REJECTED,
                Instant.now()
        );
    }

}
