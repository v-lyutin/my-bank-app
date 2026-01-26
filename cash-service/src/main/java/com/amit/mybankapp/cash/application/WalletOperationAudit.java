package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.cash.model.WalletOperationAuditRecord;
import com.amit.mybankapp.cash.application.repository.WalletOperationAuditRecordRepository;
import com.amit.mybankapp.cash.application.model.type.WalletOperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Component
public class WalletOperationAudit {

    private final WalletOperationAuditRecordRepository walletOperationAuditRecordRepository;

    @Autowired
    public WalletOperationAudit(WalletOperationAuditRecordRepository walletOperationAuditRecordRepository) {
        this.walletOperationAuditRecordRepository = walletOperationAuditRecordRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accepted(UUID operationId, String operationName, UUID walletId, UUID customerId, BigDecimal amount) {
        this.walletOperationAuditRecordRepository.save(
                new WalletOperationAuditRecord(
                        operationId,
                        operationName,
                        walletId,
                        customerId,
                        amount,
                        WalletOperationStatus.ACCEPTED.name(),
                        Instant.now()
                )
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rejected(UUID operationId, String operationName, UUID walletId, UUID customerId, BigDecimal amount) {
        this.walletOperationAuditRecordRepository.save(
                new WalletOperationAuditRecord(
                        operationId,
                        operationName,
                        walletId,
                        customerId,
                        amount,
                        WalletOperationStatus.REJECTED.name(),
                        Instant.now()
                )
        );
    }

}
