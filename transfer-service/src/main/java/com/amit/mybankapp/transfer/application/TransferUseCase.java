package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import com.amit.mybankapp.transfer.infrastructure.provider.CurrentUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferUseCase {

    private final AccountsClient accountsClient;

    private final TransferTransactionHandler transferTransactionHandler;

    private final CurrentUserProvider currentUserProvider;

    @Autowired
    public TransferUseCase(AccountsClient accountsClient,
                           TransferTransactionHandler transferTransactionHandler,
                           CurrentUserProvider currentUserProvider) {
        this.accountsClient = accountsClient;
        this.transferTransactionHandler = transferTransactionHandler;
        this.currentUserProvider = currentUserProvider;
    }

    public CreateTransferResponse createTransferWithAudit(CreateTransferRequest request) {
        UUID currentCustomerId = this.currentUserProvider.currentCustomerId();
        UUID transferId = UUID.randomUUID();

        CreateTransferRequest enrichedCreateTransferRequest = enrichWithSenderCustomerId(currentCustomerId, request);

        try {
            CreateTransferResponse createTransferResponse = this.accountsClient.createTransfer(enrichedCreateTransferRequest);

            return this.transferTransactionHandler.onAccepted(transferId, createTransferResponse);

        } catch (ApiException exception) {
            this.transferTransactionHandler.onRejected(transferId, currentCustomerId, request);

            if (exception.status().is4xxClientError()) {
                throw exception;
            }
            throw new TransferExecutionException(transferId, exception);

        } catch (RuntimeException exception) {
            this.transferTransactionHandler.onRejected(transferId, currentCustomerId, request);
            throw new TransferExecutionException(transferId, exception);
        }
    }

    private static CreateTransferRequest enrichWithSenderCustomerId(UUID currentCustomerId, CreateTransferRequest createTransferRequest) {
        return new CreateTransferRequest(currentCustomerId, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());
    }

}
