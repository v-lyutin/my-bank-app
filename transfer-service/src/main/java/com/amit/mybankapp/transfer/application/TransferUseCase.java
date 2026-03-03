package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import com.amit.mybankapp.transfer.infrastructure.provider.CurrentUserProvider;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferUseCase {

    private final AccountsClient accountsClient;

    private final TransferTransactionHandler transferTransactionHandler;

    private final CurrentUserProvider currentUserProvider;

    private final MeterRegistry meterRegistry;

    @Autowired
    public TransferUseCase(AccountsClient accountsClient,
                           TransferTransactionHandler transferTransactionHandler,
                           CurrentUserProvider currentUserProvider,
                           MeterRegistry meterRegistry) {
        this.accountsClient = accountsClient;
        this.transferTransactionHandler = transferTransactionHandler;
        this.currentUserProvider = currentUserProvider;
        this.meterRegistry = meterRegistry;
    }

    public CreateTransferResponse createTransferWithAudit(CreateTransferRequest request) {
        UUID currentCustomerId = this.currentUserProvider.currentCustomerId();
        UUID transferId = UUID.randomUUID();

        CreateTransferRequest enrichedCreateTransferRequest = enrichWithSenderCustomerId(currentCustomerId, request);

        try {
            CreateTransferResponse createTransferResponse = this.accountsClient.createTransfer(enrichedCreateTransferRequest);

            return this.transferTransactionHandler.onAccepted(transferId, createTransferResponse);

        } catch (ApiException exception) {
            this.recordTransferFailed(currentCustomerId, request.recipientCustomerId());
            this.transferTransactionHandler.onRejected(transferId, currentCustomerId, request);

            if (exception.status().is4xxClientError()) {
                throw exception;
            }
            throw new TransferExecutionException(transferId, exception);

        } catch (RuntimeException exception) {
            this.recordTransferFailed(currentCustomerId, request.recipientCustomerId());
            this.transferTransactionHandler.onRejected(transferId, currentCustomerId, request);
            throw new TransferExecutionException(transferId, exception);
        }
    }

    private static CreateTransferRequest enrichWithSenderCustomerId(UUID currentCustomerId, CreateTransferRequest createTransferRequest) {
        return new CreateTransferRequest(currentCustomerId, createTransferRequest.recipientCustomerId(), createTransferRequest.amount());
    }

    private void recordTransferFailed(UUID senderId, UUID recipientId) {
        Counter.builder("mybank.transfer.failed")
                .description("Total number of failed transfer attempts")
                .tag("sender_id", senderId.toString())
                .tag("recipient_id", recipientId.toString())
                .register(this.meterRegistry)
                .increment();
    }

}
