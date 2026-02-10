package com.amit.mybankapp.transfer.infrastructure.outbox.repository;

import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransferOutboxRepository {

    void save(TransferOutboxRecord record);

    List<TransferOutboxRecord> acquirePending(int limit, Instant processingStartedAt);

    boolean markAsSent(UUID id, Instant processedAt);

    boolean markAsFailed(UUID id);

}
