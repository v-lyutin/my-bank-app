package com.amit.mybankapp.accounts.domain.customer.vo.exception;

public final class InvalidLoginException extends RuntimeException {

    public InvalidLoginException(String message) {
        super(message);
    }

}
