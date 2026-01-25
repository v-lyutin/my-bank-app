package com.amit.mybankapp.transfer.infrastructure.client;

import com.amit.mybankapp.transfer.application.client.accounts.AccountsClient;
import com.amit.mybankapp.transfer.application.client.accounts.dto.AccountsTransferRequest;
import com.amit.mybankapp.transfer.application.client.accounts.dto.AccountsTransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class RestClientAccountsClient implements AccountsClient {

    private final RestClient restClient;

    @Autowired
    public RestClientAccountsClient(@Qualifier(value = "accountsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public AccountsTransferResponse transfer(UUID recipientCustomerId, BigDecimal amount) {
        return restClient
                .post()
                .uri("/transfers")
                .body(new AccountsTransferRequest(recipientCustomerId, amount))
                .retrieve()
                .body(AccountsTransferResponse.class);
    }

}
