package com.amit.mybankapp.transfer.infrastructure.audit;

import com.amit.mybankapp.transfer.infrastructure.audit.model.TransferAuditRecord;
import com.amit.mybankapp.transfer.infrastructure.audit.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class TransferAudit {

    private final TransferRepository transferRepository;

    @Autowired
    public TransferAudit(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void accepted(UUID transferId, UUID senderCustomerId, UUID recipientCustomerId, BigDecimal amount) {
        this.transferRepository.save(TransferAuditRecord.accepted(transferId, senderCustomerId, recipientCustomerId, amount));
    }

    public void rejected(UUID transferId, UUID senderCustomerId, UUID recipientCustomerId, BigDecimal amount) {
        this.transferRepository.save(TransferAuditRecord.rejected(transferId, senderCustomerId, recipientCustomerId, amount));
    }

}
