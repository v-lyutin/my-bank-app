package com.amit.mybankapp.accounts.application.exception;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;

public final class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forAccount(CustomerId customerId) {
        return new ResourceNotFoundException("Account not found for userId=" + customerId.value());
    }

    public static ResourceNotFoundException forWallet(WalletId walletId) {
        return new ResourceNotFoundException("Wallet not found for walletId=" + walletId.value());
    }

}
