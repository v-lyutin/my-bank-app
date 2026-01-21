package com.amit.mybankapp.transfer.infrastructure.client;

import com.amit.mybankapp.transfer.application.client.accounts.AccountsClient;
import com.amit.mybankapp.transfer.application.client.accounts.dto.AccountsTransferResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StubAccountsClient implements AccountsClient {

    @Override
    public AccountsTransferResponse transfer(UUID recipientCustomerId, BigDecimal amount) {
        UUID senderCustomerId = UUID.randomUUID();

        return new AccountsTransferResponse(
                senderCustomerId,
                recipientCustomerId,
                amount
        );
    }

}
