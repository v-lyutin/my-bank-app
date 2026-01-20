package com.amit.mybankapp.cash.application.model.exception;

public final class InvalidCashCommandException extends RuntimeException {

    public InvalidCashCommandException(String message) {
        super(message);
    }

}
