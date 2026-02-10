package com.amit.mybankapp.transfer.infrastructure.audit.repository.jdbc.mapper;

import com.amit.mybankapp.transfer.infrastructure.audit.model.TransferAuditRecord;
import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class TransferAuditRowMapper implements RowMapper<TransferAuditRecord> {

    @Override
    public TransferAuditRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new TransferAuditRecord(
                (UUID) resultSet.getObject("transfer_id"),
                (UUID) resultSet.getObject("sender_customer_id"),
                (UUID) resultSet.getObject("recipient_customer_id"),
                resultSet.getBigDecimal("amount"),
                TransferStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toInstant()
        );
    }

}
