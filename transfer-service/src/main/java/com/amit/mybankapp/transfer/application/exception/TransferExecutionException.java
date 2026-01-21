package com.amit.mybankapp.transfer.application.exception;

import java.util.UUID;

public final class TransferExecutionException extends RuntimeException {

    public TransferExecutionException(UUID transferId, Throwable cause) {
        super("Transfer " + transferId + " execution failed", cause);
    }

}
