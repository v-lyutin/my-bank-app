package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.cash.model.CashOperationAuditRecord;
import com.amit.mybankapp.cash.application.repository.CashOperationAuditRecordRepository;
import com.amit.mybankapp.cash.application.model.type.CashCommandType;
import com.amit.mybankapp.cash.application.model.type.CashStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Component
public class CashOperationAudit {

    private final CashOperationAuditRecordRepository cashOperationAuditRecordRepository;

    private final Clock clock;

    @Autowired
    public CashOperationAudit(CashOperationAuditRecordRepository cashOperationAuditRecordRepository, Clock clock) {
        this.cashOperationAuditRecordRepository = cashOperationAuditRecordRepository;
        this.clock = clock;
    }

    public void accepted(UUID operationId, UUID walletId, CashCommandType type, BigDecimal amount) {
        this.cashOperationAuditRecordRepository.save(
                new CashOperationAuditRecord(
                        operationId,
                        walletId,
                        type.name(),
                        amount,
                        CashStatus.ACCEPTED.name(),
                        Instant.now(this.clock)
                )
        );
    }

    public void rejected(UUID operationId, UUID walletId, CashCommandType type, BigDecimal amount) {
        this.cashOperationAuditRecordRepository.save(
                new CashOperationAuditRecord(
                        operationId,
                        walletId,
                        type.name(),
                        amount,
                        CashStatus.REJECTED.name(),
                        Instant.now(this.clock)
                )
        );
    }

}
