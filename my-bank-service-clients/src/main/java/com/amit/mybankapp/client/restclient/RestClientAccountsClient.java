package com.amit.mybankapp.client.restclient;

import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

public class RestClientAccountsClient implements AccountsClient {

    private final RestClient restClient;

    public RestClientAccountsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CustomerResponse getCustomerByCustomerId(UUID customerId) {
        return this.restClient.get()
                .uri("/internal/customers/{customerId}", customerId)
                .retrieve()
                .body(CustomerResponse.class);
    }

    @Override
    public CustomerResponse getCurrentCustomer() {
        return this.restClient.get()
                .uri("/customers/me")
                .retrieve()
                .body(CustomerResponse.class);
    }

    @Override
    public CustomerResponse updateProfileForCurrentCustomer(UpdateProfileRequest updateProfileRequest) {
        return this.restClient.put()
                .uri("/customers/me/profile")
                .body(updateProfileRequest)
                .retrieve()
                .body(CustomerResponse.class);
    }

    @Override
    public List<CustomerLookupResponse> getTransferRecipientsByCurrentCustomer() {
        CustomerLookupResponse[] response = this.restClient.get()
                .uri("/customers/me/recipient-candidates")
                .retrieve()
                .body(CustomerLookupResponse[].class);
        return response == null ? List.of() : List.of(response);
    }

    @Override
    public CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest) {
        return this.restClient.post()
                .uri("/internal/transfers")
                .body(createTransferRequest)
                .retrieve()
                .body(CreateTransferResponse.class);
    }

    public WalletResponse getPrimaryWalletForCurrentUser() {
        return this.restClient.get()
                .uri("/wallets/me")
                .retrieve()
                .body(WalletResponse.class);
    }

    @Override
    public WalletOperationResponse deposit(UUID customerId, WalletOperationRequest walletOperationRequest) {
        return this.restClient.post()
                .uri("/internal/wallets/{customerId}/deposits", customerId)
                .body(walletOperationRequest)
                .retrieve()
                .body(WalletOperationResponse.class);
    }

    @Override
    public WalletOperationResponse withdraw(UUID customerId, WalletOperationRequest walletOperationRequest) {
        return this.restClient.post()
                .uri("/internal/wallets/{customerId}/withdrawals", customerId)
                .body(walletOperationRequest)
                .retrieve()
                .body(WalletOperationResponse.class);
    }

}
