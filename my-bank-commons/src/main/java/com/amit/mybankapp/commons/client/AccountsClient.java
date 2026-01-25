package com.amit.mybankapp.commons.client;

import com.amit.mybankapp.commons.client.dto.accounts.CustomerLookupResponse;
import com.amit.mybankapp.commons.client.dto.accounts.CustomerResponse;
import com.amit.mybankapp.commons.client.dto.accounts.UpdateProfileRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsClient {

    CustomerResponse getCurrentCustomer();

    UpdateProfileRequest updateProfile();

    List<CustomerLookupResponse> getTransferRecipients();

    WalletResponse deposit(BigDecimal amount);

    WalletResponse withdraw(BigDecimal amount);

    CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest);

}
