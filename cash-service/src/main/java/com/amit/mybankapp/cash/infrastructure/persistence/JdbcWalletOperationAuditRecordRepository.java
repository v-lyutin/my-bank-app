package com.amit.mybankapp.cash.infrastructure.persistence;

import com.amit.mybankapp.cash.model.WalletOperationAuditRecord;
import com.amit.mybankapp.cash.application.repository.WalletOperationAuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JdbcWalletOperationAuditRecordRepository implements WalletOperationAuditRecordRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcWalletOperationAuditRecordRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void save(WalletOperationAuditRecord record) {
        this.namedParameterJdbcTemplate.update(
                SqlWalletOperationAuditRecordQuery.INSERT,
                Map.of(
                        "operationId", record.operationId(),
                        "walletId", record.walletId(),
                        "customerId", record.customerId(),
                        "operationType", record.operationType(),
                        "amount", record.amount(),
                        "status", record.status(),
                        "createdAt", record.createdAt()
                )
        );
    }

}
