package com.amit.mybankapp.cash.application.exception;

import java.util.UUID;

public final class CashOperationExecutionException extends RuntimeException {

    public CashOperationExecutionException(UUID operationId, Throwable cause) {
        super("Cash operation " + operationId + " execution failed", cause);
    }

}
