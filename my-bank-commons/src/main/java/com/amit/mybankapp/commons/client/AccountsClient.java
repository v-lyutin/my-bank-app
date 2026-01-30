package com.amit.mybankapp.commons.client;

import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;

import java.util.List;
import java.util.UUID;

public interface AccountsClient {

    CustomerResponse getCustomerByCustomerId(UUID customerId);

    CustomerResponse getCurrentCustomer();

    CustomerResponse updateProfileForCurrentCustomer(UpdateProfileRequest updateProfileRequest);

    List<CustomerLookupResponse> getTransferRecipientsByCurrentCustomer();

    CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest);

    WalletResponse getPrimaryWalletForCurrentUser();

    WalletOperationResponse deposit(UUID customerId, WalletOperationRequest walletOperationRequest);

    WalletOperationResponse withdraw(UUID customerId, WalletOperationRequest walletOperationRequest);

}
