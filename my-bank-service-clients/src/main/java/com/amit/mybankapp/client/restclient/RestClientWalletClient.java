package com.amit.mybankapp.client.restclient;

import com.amit.mybankapp.commons.client.WalletClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import org.springframework.web.client.RestClient;

public class RestClientWalletClient implements WalletClient {

    private final RestClient restClient;

    public RestClientWalletClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public WalletOperationResponse deposit(WalletOperationRequest walletOperationRequest) {
        return this.restClient.post()
                .uri("/customers/me/deposits")
                .body(walletOperationRequest)
                .retrieve()
                .body(WalletOperationResponse.class);
    }

    @Override
    public WalletOperationResponse withdraw(WalletOperationRequest walletOperationRequest) {
        return this.restClient.post()
                .uri("/customers/me/withdrawals")
                .body(walletOperationRequest)
                .retrieve()
                .body(WalletOperationResponse.class);
    }

}
