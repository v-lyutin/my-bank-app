package com.amit.mybankapp.transfer.infrastructure.outbox.repository.jdbc.mapper;

import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public final class TransferOutboxRowMapper implements RowMapper<TransferOutboxRecord> {

    @Override
    public TransferOutboxRecord mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Timestamp processingStartedAt = resultSet.getTimestamp("processing_started_at");
        Timestamp processedAt = resultSet.getTimestamp("processed_at");

        return new TransferOutboxRecord(
                (UUID) resultSet.getObject("id"),
                (UUID) resultSet.getObject("transfer_id"),
                TransferOutboxRecord.TransferEventType.valueOf(resultSet.getString("event_type")),
                resultSet.getString("payload"),
                TransferOutboxRecord.TransferOutboxStatus.valueOf(resultSet.getString("status")),
                resultSet.getTimestamp("created_at").toInstant(),
                processingStartedAt == null ? null : processingStartedAt.toInstant(),
                processedAt == null ? null : processedAt.toInstant()
        );
    }

}
