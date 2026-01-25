package com.amit.mybankapp.transfer.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.transfer.application.client.accounts.AccountsClient;
import com.amit.mybankapp.transfer.application.client.accounts.dto.AccountsTransferResponse;
import com.amit.mybankapp.transfer.application.client.notifications.NotificationsClient;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import com.amit.mybankapp.transfer.application.model.TransferCommand;
import com.amit.mybankapp.transfer.application.model.TransferResult;
import com.amit.mybankapp.transfer.infrastructure.audit.TransferAudit;
import com.amit.mybankapp.transfer.infrastructure.audit.model.type.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferUseCase {

    private final AccountsClient accountsClient;

    private final NotificationsClient notificationsClient;

    private final TransferAudit transferAudit;

    @Autowired
    public TransferUseCase(AccountsClient accountsClient,
                           NotificationsClient notificationsClient,
                           TransferAudit transferAudit) {
        this.accountsClient = accountsClient;
        this.notificationsClient = notificationsClient;
        this.transferAudit = transferAudit;
    }

    @Transactional
    public TransferResult transfer(TransferCommand command) {
        UUID transferId = UUID.randomUUID();

        try {
            AccountsTransferResponse accountsTransferResponse = this.accountsClient.transfer(command.recipientCustomerId(), command.amount());

            this.notificationsClient.sendTransferSent(accountsTransferResponse.senderCustomerId(), accountsTransferResponse.amount());
            this.notificationsClient.sendTransferReceived(accountsTransferResponse.recipientCustomerId(), accountsTransferResponse.amount());

            this.transferAudit.accepted(
                    transferId,
                    accountsTransferResponse.senderCustomerId(),
                    accountsTransferResponse.recipientCustomerId(),
                    accountsTransferResponse.amount()
            );

            return new TransferResult(transferId, TransferStatus.ACCEPTED);

        } catch (ApiException exception) {
            this.transferAudit.rejected(transferId, null, command.recipientCustomerId(), command.amount());

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new TransferExecutionException(transferId, exception);

        } catch (RuntimeException exception) {
            this.transferAudit.rejected(transferId, null, command.recipientCustomerId(), command.amount());
            throw new TransferExecutionException(transferId, exception);
        }
    }

}
