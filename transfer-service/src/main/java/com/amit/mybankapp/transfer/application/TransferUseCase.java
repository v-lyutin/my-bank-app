package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import com.amit.mybankapp.transfer.infrastructure.audit.TransferAudit;
import com.amit.mybankapp.transfer.infrastructure.provider.CurrentUserProvider;
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

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public TransferUseCase(AccountsClient accountsClient,
                           ApplicationEventPublisher applicationEventPublisher,
                           TransferAudit transferAudit,
                           CurrentUserProvider currentUserProvider) {
        this.accountsClient = accountsClient;
        this.applicationEventPublisher = applicationEventPublisher;
        this.transferAudit = transferAudit;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public CreateTransferResponse createTransferWithAudit(CreateTransferRequest createTransferRequest) {
        UUID currentCustomerId = this.currentUserProvider.currentCustomerId();
        UUID transferId = UUID.randomUUID();

        try {
            CreateTransferRequest enrichedCreateTransferRequest = enrichWithSenderCustomerId(currentCustomerId, createTransferRequest);

            CreateTransferResponse createTransferResponse = this.accountsClient.createTransfer(enrichedCreateTransferRequest);

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
            this.transferAudit.rejected(transferId, currentCustomerId, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new TransferExecutionException(transferId, exception);
        } catch (RuntimeException exception) {
            this.transferAudit.rejected(transferId, currentCustomerId, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());
            throw new TransferExecutionException(transferId, exception);
        }
    }

    private static CreateTransferRequest enrichWithSenderCustomerId(UUID currentCustomerId, CreateTransferRequest createTransferRequest) {
        return new CreateTransferRequest(
                currentCustomerId,
                createTransferRequest.recipientCustomerId(),
                createTransferRequest.amount()
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
