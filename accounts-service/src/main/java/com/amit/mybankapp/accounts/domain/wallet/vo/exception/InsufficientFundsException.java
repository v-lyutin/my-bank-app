package com.amit.mybankapp.accounts.domain.wallet.vo.exception;

public final class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("insufficient funds");
    }

}
