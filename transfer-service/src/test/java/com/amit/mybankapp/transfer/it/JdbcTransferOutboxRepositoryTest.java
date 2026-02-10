package com.amit.mybankapp.transfer.it;

import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.jdbc.JdbcTransferOutboxRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = JdbcTransferOutboxRepository.class)
public class JdbcTransferOutboxRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private JdbcTransferOutboxRepository jdbcTransferOutboxRepository;

    @Test
    @DisplayName(value = "Should save outbox record when record is valid")
    void save_shouldSaveOutboxRecordWhenRecordIsValid() {
        UUID outboxId = UUID.randomUUID();
        UUID transferId = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-21T10:11:12Z");

        TransferOutboxRecord record = new TransferOutboxRecord(
                outboxId,
                transferId,
                TransferOutboxRecord.TransferEventType.TRANSFER_CREATED,
                "{\"transferId\":\"" + transferId + "\"}",
                TransferOutboxRecord.TransferOutboxStatus.PENDING,
                createdAt,
                null,
                null
        );

        this.jdbcTransferOutboxRepository.save(record);

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT id, transfer_id, event_type, payload, status, created_at, processing_started_at, processed_at
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", outboxId)
        );

        assertThat(row.get("id")).isEqualTo(outboxId);
        assertThat(row.get("transfer_id")).isEqualTo(transferId);
        assertThat(row.get("event_type")).isEqualTo(TransferOutboxRecord.TransferEventType.TRANSFER_CREATED.name());
        assertThat(row.get("payload")).isNotNull();
        assertThat(row.get("status")).isEqualTo(TransferOutboxRecord.TransferOutboxStatus.PENDING.name());
        assertThat(((Timestamp) row.get("created_at")).toInstant()).isEqualTo(createdAt);
        assertThat(row.get("processing_started_at")).isNull();
        assertThat(row.get("processed_at")).isNull();
    }

    @Test
    @DisplayName(value = "Should acquire oldest pending events and mark them as PROCESSING")
    void acquirePending_shouldAcquireOldestPendingAndMarkAsProcessing() {
        Instant base = Instant.parse("2026-01-21T10:00:00Z");

        UUID pendingOldestId = UUID.randomUUID();
        UUID pendingNewestId = UUID.randomUUID();
        UUID sentId = UUID.randomUUID();

        UUID transfer1 = UUID.randomUUID();
        UUID transfer2 = UUID.randomUUID();
        UUID transfer3 = UUID.randomUUID();

        insertOutboxRow(pendingOldestId, transfer1, "TRANSFER_CREATED", "{\"a\":1}", "PENDING",
                base.plusSeconds(1), null, null);

        insertOutboxRow(pendingNewestId, transfer2, "TRANSFER_CREATED", "{\"a\":2}", "PENDING",
                base.plusSeconds(10), null, null);

        insertOutboxRow(sentId, transfer3, "TRANSFER_CREATED", "{\"a\":3}", "SENT",
                base.plusSeconds(0), base.plusSeconds(5), base.plusSeconds(6));

        Instant processingStartedAt = Instant.parse("2026-01-21T11:00:00Z");

        List<TransferOutboxRecord> acquired = this.jdbcTransferOutboxRepository.acquirePending(1, processingStartedAt);

        assertThat(acquired).hasSize(1);
        assertThat(acquired.getFirst().id()).isEqualTo(pendingOldestId);
        assertThat(acquired.getFirst().status()).isEqualTo(TransferOutboxRecord.TransferOutboxStatus.PROCESSING);
        assertThat(acquired.getFirst().processingStartedAt()).isEqualTo(processingStartedAt);

        Map<String, Object> oldestRow = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status, processing_started_at
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", pendingOldestId)
        );
        assertThat(oldestRow.get("status")).isEqualTo("PROCESSING");
        assertThat(((Timestamp) oldestRow.get("processing_started_at")).toInstant()).isEqualTo(processingStartedAt);

        Map<String, Object> newestRow = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status, processing_started_at
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", pendingNewestId)
        );
        assertThat(newestRow.get("status")).isEqualTo("PENDING");
        assertThat(newestRow.get("processing_started_at")).isNull();

        Map<String, Object> sentRow = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", sentId)
        );
        assertThat(sentRow.get("status")).isEqualTo("SENT");
    }

    @Test
    @DisplayName(value = "Should mark event as SENT when current status is PROCESSING")
    void markAsSent_shouldUpdateWhenStatusIsProcessing() {
        UUID id = UUID.randomUUID();
        UUID transferId = UUID.randomUUID();

        Instant createdAt = Instant.parse("2026-01-21T10:00:00Z");
        Instant processingStartedAt = Instant.parse("2026-01-21T10:05:00Z");
        Instant processedAt = Instant.parse("2026-01-21T10:06:00Z");

        insertOutboxRow(id, transferId, "TRANSFER_CREATED", "{\"x\":1}", "PROCESSING",
                createdAt, processingStartedAt, null);

        boolean updated = this.jdbcTransferOutboxRepository.markAsSent(id, processedAt);

        assertThat(updated).isTrue();

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status, processed_at
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", id)
        );

        assertThat(row.get("status")).isEqualTo("SENT");
        assertThat(((Timestamp) row.get("processed_at")).toInstant()).isEqualTo(processedAt);
    }

    @Test
    @DisplayName(value = "Should return false when marking as SENT and current status is not PROCESSING")
    void markAsSent_shouldReturnFalseWhenStatusIsNotProcessing() {
        UUID id = UUID.randomUUID();
        UUID transferId = UUID.randomUUID();

        insertOutboxRow(
                id, transferId, "TRANSFER_CREATED", "{\"x\":1}", "PENDING",
                Instant.parse("2026-01-21T10:00:00Z"), null, null
        );

        boolean updated = this.jdbcTransferOutboxRepository.markAsSent(id, Instant.parse("2026-01-21T10:06:00Z"));

        assertThat(updated).isFalse();

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status, processed_at
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", id)
        );

        assertThat(row.get("status")).isEqualTo("PENDING");
        assertThat(row.get("processed_at")).isNull();
    }

    @Test
    @DisplayName(value = "Should mark event as FAILED when current status is PROCESSING")
    void markAsFailed_shouldUpdateWhenStatusIsProcessing() {
        UUID id = UUID.randomUUID();
        UUID transferId = UUID.randomUUID();

        insertOutboxRow(
                id, transferId, "TRANSFER_CREATED", "{\"x\":1}", "PROCESSING",
                Instant.parse("2026-01-21T10:00:00Z"),
                Instant.parse("2026-01-21T10:05:00Z"),
                null
        );

        boolean updated = this.jdbcTransferOutboxRepository.markAsFailed(id);

        assertThat(updated).isTrue();

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", id)
        );

        assertThat(row.get("status")).isEqualTo("FAILED");
    }

    @Test
    @DisplayName(value = "Should return false when marking as FAILED and current status is not PROCESSING")
    void markAsFailed_shouldReturnFalseWhenStatusIsNotProcessing() {
        UUID id = UUID.randomUUID();
        UUID transferId = UUID.randomUUID();

        insertOutboxRow(
                id, transferId, "TRANSFER_CREATED", "{\"x\":1}", "PENDING",
                Instant.parse("2026-01-21T10:00:00Z"),
                null,
                null
        );

        boolean updated = this.jdbcTransferOutboxRepository.markAsFailed(id);

        assertThat(updated).isFalse();

        Map<String, Object> row = this.namedParameterJdbcTemplate.queryForMap(
                """
                        SELECT status
                        FROM transfers.transfer_outbox
                        WHERE id = :id
                        """,
                Map.of("id", id)
        );

        assertThat(row.get("status")).isEqualTo("PENDING");
    }

    private void insertOutboxRow(UUID id,
                                 UUID transferId,
                                 String eventType,
                                 String payload,
                                 String status,
                                 Instant createdAt,
                                 Instant processingStartedAt,
                                 Instant processedAt) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("transferId", transferId)
                .addValue("eventType", eventType)
                .addValue("payload", payload)
                .addValue("status", status)
                .addValue("createdAt", Timestamp.from(createdAt))
                .addValue("processingStartedAt", processingStartedAt == null ? null : Timestamp.from(processingStartedAt))
                .addValue("processedAt", processedAt == null ? null : Timestamp.from(processedAt));

        this.namedParameterJdbcTemplate.update(
                """
                        INSERT INTO transfers.transfer_outbox (
                            id, transfer_id, event_type, payload, status, created_at, processing_started_at, processed_at
                        ) VALUES (
                            :id, :transferId, :eventType, CAST(:payload AS jsonb), :status, :createdAt, :processingStartedAt, :processedAt
                        )
                        """,
                params
        );
    }

}
