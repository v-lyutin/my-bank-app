package com.amit.mybankapp.accounts.application.exception;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;

public final class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forAccount(CustomerId customerId) {
        return new ResourceNotFoundException("Account not found for userId=" + customerId.value());
    }

    public static ResourceNotFoundException forWalletOfCustomer(CustomerId customerId) {
        return new ResourceNotFoundException("Wallet not found for customerId=" + customerId.value());
    }

}
