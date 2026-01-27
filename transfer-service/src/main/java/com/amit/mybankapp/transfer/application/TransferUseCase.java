package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import com.amit.mybankapp.transfer.infrastructure.audit.TransferAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferUseCase {

    private final AccountsClient accountsClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TransferAudit transferAudit;

    @Autowired
    public TransferUseCase(AccountsClient accountsClient,
                           ApplicationEventPublisher applicationEventPublisher,
                           TransferAudit transferAudit) {
        this.accountsClient = accountsClient;
        this.applicationEventPublisher = applicationEventPublisher;
        this.transferAudit = transferAudit;
    }

    @Transactional
    public CreateTransferResponse createTransferWithAudit(CreateTransferRequest createTransferRequest) {
        UUID transferId = UUID.randomUUID();

        try {
            CreateTransferResponse createTransferResponse = this.accountsClient.createTransfer(createTransferRequest);

            this.transferAudit.accepted(
                    transferId,
                    createTransferResponse.senderCustomerId(),
                    createTransferResponse.recipientCustomerId(),
                    createTransferResponse.amount()
            );

            CreateTransferResponse enrichedCreateTransferResponse = enrichWithTransferId(transferId, createTransferResponse);

            this.applicationEventPublisher.publishEvent(TransferCreatedEvent.from(enrichedCreateTransferResponse));

            return enrichedCreateTransferResponse;
        } catch (ApiException exception) {
            this.transferAudit.rejected(transferId, null, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new TransferExecutionException(transferId, exception);
        } catch (RuntimeException exception) {
            this.transferAudit.rejected(transferId, null, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());
            throw new TransferExecutionException(transferId, exception);
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
