package com.amit.mybankapp.transfer.infrastructure.audit.repository;

import com.amit.mybankapp.transfer.infrastructure.audit.model.TransferAuditRecord;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository {

    void save(TransferAuditRecord transferAuditRecord);

    Optional<TransferAuditRecord> findByTransferId(UUID transferId);

}
