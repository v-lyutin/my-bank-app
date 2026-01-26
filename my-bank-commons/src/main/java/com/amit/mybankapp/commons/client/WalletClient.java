package com.amit.mybankapp.commons.client;

import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;

public interface WalletClient {

    WalletOperationResponse deposit(WalletOperationRequest walletOperationRequest);

    WalletOperationResponse withdraw(WalletOperationRequest walletOperationRequest);

}
