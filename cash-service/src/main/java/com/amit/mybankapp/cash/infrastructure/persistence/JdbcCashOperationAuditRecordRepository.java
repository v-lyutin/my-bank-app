package com.amit.mybankapp.cash.infrastructure.persistence;

import com.amit.mybankapp.cash.model.CashOperationAuditRecord;
import com.amit.mybankapp.cash.application.repository.CashOperationAuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JdbcCashOperationAuditRecordRepository implements CashOperationAuditRecordRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcCashOperationAuditRecordRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void save(CashOperationAuditRecord record) {
        this.namedParameterJdbcTemplate.update(
                SqlCashOperationAuditRecordQuery.INSERT,
                Map.of(
                        "operationId", record.operationId(),
                        "walletId", record.walletId(),
                        "type", record.type(),
                        "amount", record.amount(),
                        "status", record.status()
                )
        );
    }

}
