package com.amit.mybankapp.frontcontroller.port;

import com.amit.mybankapp.commons.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.dto.wallet.WalletResponse;

public interface WalletPort {

    WalletResponse getWallet();

    void deposit(WalletOperationRequest walletOperationRequest);

    void withdraw(WalletOperationRequest walletOperationRequest);

}
