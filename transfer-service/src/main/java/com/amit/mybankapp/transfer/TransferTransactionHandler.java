package com.amit.mybankapp.transfer;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.transfer.infrastructure.audit.TransferAudit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferTransactionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferTransactionHandler.class);

    private final TransferAudit transferAudit;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TransferTransactionHandler(TransferAudit transferAudit, ApplicationEventPublisher applicationEventPublisher) {
        this.transferAudit = transferAudit;
        this.applicationEventPublisher = applicationEventPublisher;
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

        this.applicationEventPublisher.publishEvent(TransferCreatedEvent.from(enrichedCreateTransferResponse));

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
