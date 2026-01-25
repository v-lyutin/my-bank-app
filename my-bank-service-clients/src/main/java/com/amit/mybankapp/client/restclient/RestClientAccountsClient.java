package com.amit.mybankapp.client.restclient;

import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

public class RestClientAccountsClient implements AccountsClient {

    private final RestClient restClient;

    public RestClientAccountsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CustomerResponse getCurrentCustomer() {
        return this.restClient.get()
                .uri("/customers/me")
                .retrieve()
                .body(CustomerResponse.class);
    }

    @Override
    public CustomerResponse updateProfile(UpdateProfileRequest updateProfileRequest) {
        return this.restClient.put()
                .uri("/customers/me/profile")
                .body(updateProfileRequest)
                .retrieve()
                .body(CustomerResponse.class);
    }

    @Override
    public List<CustomerLookupResponse> getTransferRecipients() {
        CustomerLookupResponse[] response = this.restClient.get()
                .uri("customers/me/recipient-candidates")
                .retrieve()
                .body(CustomerLookupResponse[].class);
        return response == null ? List.of() : List.of(response);
    }

    @Override
    public CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest) {
        return this.restClient.post()
                .uri("/transfers")
                .body(createTransferRequest)
                .retrieve()
                .body(CreateTransferResponse.class);
    }

    @Override
    public WalletResponse deposit(BigDecimal amount) {
        return this.restClient.post()
                .uri("/wallets/me/deposits")
                .body(amount)
                .retrieve()
                .body(WalletResponse.class);
    }

    @Override
    public WalletResponse withdraw(BigDecimal amount) {
        return this.restClient.post()
                .uri("/wallets/me/withdrawals")
                .body(amount)
                .retrieve()
                .body(WalletResponse.class);
    }

}
