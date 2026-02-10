package com.amit.mybankapp.transfer.infrastructure.audit.repository.jdbc;

import com.amit.mybankapp.transfer.infrastructure.audit.model.TransferAuditRecord;
import com.amit.mybankapp.transfer.infrastructure.audit.repository.TransferRepository;
import com.amit.mybankapp.transfer.infrastructure.audit.repository.jdbc.mapper.TransferAuditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcTransferRepository implements TransferRepository {

    private static final TransferAuditRowMapper TRANSFER_AUDIT_ROW_MAPPER = new TransferAuditRowMapper();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcTransferRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void save(TransferAuditRecord record) {
        this.namedParameterJdbcTemplate.update(
                SqlTransferQuery.INSERT_TRANSFER_OPERATION,
                Map.of(
                        "transferId", record.transferId(),
                        "senderCustomerId", record.senderCustomerId(),
                        "recipientCustomerId", record.recipientCustomerId(),
                        "amount", record.amount(),
                        "status", record.status().name(),
                        "createdAt", Timestamp.from(record.createdAt())
                )
        );
    }

    @Override
    public Optional<TransferAuditRecord> findByTransferId(UUID transferId) {
        List<TransferAuditRecord> result = this.namedParameterJdbcTemplate.query(
                SqlTransferQuery.FIND_BY_TRANSFER_ID,
                Map.of("transferId", transferId),
                TRANSFER_AUDIT_ROW_MAPPER
        );
        return result.stream().findFirst();
    }

}
