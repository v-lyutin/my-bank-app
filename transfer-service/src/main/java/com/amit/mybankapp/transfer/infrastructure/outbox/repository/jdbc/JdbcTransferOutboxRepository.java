package com.amit.mybankapp.transfer.infrastructure.outbox.repository.jdbc;

import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.TransferOutboxRepository;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.jdbc.mapper.TransferOutboxRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class JdbcTransferOutboxRepository implements TransferOutboxRepository {

    private static final TransferOutboxRowMapper TRANSFER_OUTBOX_ROW_MAPPER = new TransferOutboxRowMapper();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcTransferOutboxRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void save(TransferOutboxRecord record) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", record.id())
                .addValue("transferId", record.transferId())
                .addValue("eventType", record.eventType().name())
                .addValue("payload", record.payload())
                .addValue("status", record.status().name())
                .addValue("createdAt", Timestamp.from(record.createdAt()))
                .addValue(
                        "processingStartedAt",
                        record.processingStartedAt() == null ? null : Timestamp.from(record.processingStartedAt())
                )
                .addValue(
                        "processedAt",
                        record.processedAt() == null ? null : Timestamp.from(record.processedAt())
                );

        this.namedParameterJdbcTemplate.update(SqlTransferOutboxQuery.INSERT_OUTBOX_EVENT, params);
    }

    @Override
    public List<TransferOutboxRecord> acquirePending(int limit, Instant processingStartedAt) {
        return this.namedParameterJdbcTemplate.query(
                SqlTransferOutboxQuery.ACQUIRE_PENDING_EVENTS,
                Map.of(
                        "pendingStatus", TransferOutboxRecord.TransferOutboxStatus.PENDING.name(),
                        "processingStatus", TransferOutboxRecord.TransferOutboxStatus.PROCESSING.name(),
                        "processingStartedAt", Timestamp.from(processingStartedAt),
                        "limit", limit
                ),
                TRANSFER_OUTBOX_ROW_MAPPER
        );
    }

    @Override
    public boolean markAsSent(UUID id, Instant processedAt) {
        int updatedRows = this.namedParameterJdbcTemplate.update(
                SqlTransferOutboxQuery.MARK_EVENT_SENT,
                Map.of(
                        "id", id,
                        "processingStatus", TransferOutboxRecord.TransferOutboxStatus.PROCESSING.name(),
                        "sentStatus", TransferOutboxRecord.TransferOutboxStatus.SENT.name(),
                        "processedAt", Timestamp.from(processedAt)
                )
        );
        return updatedRows == 1;
    }

    @Override
    public boolean markAsFailed(UUID id) {
        int updatedRows = this.namedParameterJdbcTemplate.update(
                SqlTransferOutboxQuery.MARK_EVENT_FAILED,
                Map.of(
                        "id", id,
                        "processingStatus", TransferOutboxRecord.TransferOutboxStatus.PROCESSING.name(),
                        "failedStatus", TransferOutboxRecord.TransferOutboxStatus.FAILED.name()
                )
        );
        return updatedRows == 1;
    }

}
