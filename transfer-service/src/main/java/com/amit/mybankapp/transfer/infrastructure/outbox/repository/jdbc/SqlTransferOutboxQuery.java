package com.amit.mybankapp.transfer.infrastructure.outbox.repository.jdbc;

public final class SqlTransferOutboxQuery {

    static final String INSERT_OUTBOX_EVENT = """
            INSERT INTO transfers.transfer_outbox (
                id,
                transfer_id,
                event_type,
                payload,
                status,
                created_at,
                processing_started_at,
                processed_at
            ) VALUES (
                :id,
                :transferId,
                :eventType,
                CAST(:payload AS jsonb),
                :status,
                :createdAt,
                :processingStartedAt,
                :processedAt
            )
            """;

    static final String ACQUIRE_PENDING_EVENTS = """
            UPDATE transfers.transfer_outbox
            SET status = :processingStatus,
                processing_started_at = :processingStartedAt
            WHERE id IN (
                SELECT id
                FROM transfers.transfer_outbox
                WHERE status = :pendingStatus
                ORDER BY created_at
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            )
            RETURNING id,
                      transfer_id,
                      event_type,
                      payload,
                      status,
                      created_at,
                      processing_started_at,
                      processed_at
            """;

    static final String MARK_EVENT_SENT = """
            UPDATE transfers.transfer_outbox
            SET status = :sentStatus,
                processed_at = :processedAt
            WHERE id = :id
              AND status = :processingStatus
            """;

    static final String MARK_EVENT_FAILED = """
            UPDATE transfers.transfer_outbox
            SET status = :failedStatus
            WHERE id = :id
              AND status = :processingStatus
            """;

    private SqlTransferOutboxQuery() {
        throw new UnsupportedOperationException();
    }

}
