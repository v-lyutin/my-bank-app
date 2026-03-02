package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.transfer.infrastructure.audit.TransferAudit;
import com.amit.mybankapp.transfer.infrastructure.outbox.model.TransferOutboxRecord;
import com.amit.mybankapp.transfer.infrastructure.outbox.producer.event.TransferCreatedEvent;
import com.amit.mybankapp.transfer.infrastructure.outbox.repository.TransferOutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TransferTransactionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferTransactionHandler.class);

    private final TransferAudit transferAudit;

    private final TransferOutboxRepository transferOutboxRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public TransferTransactionHandler(TransferAudit transferAudit,
                                      TransferOutboxRepository transferOutboxRepository,
                                      ObjectMapper objectMapper) {
        this.transferAudit = transferAudit;
        this.transferOutboxRepository = transferOutboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CreateTransferResponse onAccepted(UUID transferId, CreateTransferResponse response) {
        this.transferAudit.accepted(
                transferId,
                response.senderCustomerId(),
                response.recipientCustomerId(),
                response.amount()
        );

        CreateTransferResponse enrichedCreateTransferResponse = enrichWithTransferId(transferId, response);

        TransferOutboxRecord transferOutboxRecord = TransferOutboxRecord.pending(
                UUID.randomUUID(),
                transferId,
                TransferOutboxRecord.TransferEventType.TRANSFER_CREATED,
                serialize(TransferCreatedEvent.from(enrichedCreateTransferResponse)),
                Instant.now()
        );

        this.transferOutboxRepository.save(transferOutboxRecord);

        LOGGER.info(
                "Transfer accepted: transferId={}, senderCustomerId={}, recipientCustomerId={}, amount={}",
                transferId,
                response.senderCustomerId(),
                response.recipientCustomerId(),
                response.amount()
        );

        return enrichedCreateTransferResponse;
    }

    @Transactional
    public void onRejected(UUID transferId, UUID currentCustomerId, CreateTransferRequest request) {
        this.transferAudit.rejected(
                transferId,
                currentCustomerId,
                request.recipientCustomerId(),
                request.amount()
        );
    }

    private String serialize(Object event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize outbox payload", exception);
        }
    }

    private static CreateTransferResponse enrichWithTransferId(UUID transferId, CreateTransferResponse createTransferResponse) {
        return new CreateTransferResponse(
                transferId,
                createTransferResponse.senderCustomerId(),
                createTransferResponse.recipientCustomerId(),
                createTransferResponse.amount(),
                createTransferResponse.status()
        );
    }

}
