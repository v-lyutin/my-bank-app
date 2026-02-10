package com.amit.mybankapp.transfer.infrastructure.outbox.processor;

import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.TransferOutboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class TransferOutboxAcquirer {

    private final TransferOutboxRepository transferOutboxRepository;

    @Autowired
    public TransferOutboxAcquirer(TransferOutboxRepository transferOutboxRepository) {
        this.transferOutboxRepository = transferOutboxRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<TransferOutboxRecord> acquirePendingEvents(int batchSize) {
        return this.transferOutboxRepository.acquirePending(batchSize, Instant.now());
    }

}
