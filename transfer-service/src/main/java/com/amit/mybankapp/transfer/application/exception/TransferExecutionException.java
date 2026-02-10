package com.amit.mybankapp.transfer.application.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class TransferExecutionException extends ApiException {

    public TransferExecutionException(UUID transferId, Throwable cause) {
        super("Transfer " + transferId + " execution failed", cause);
    }

    @Override
    public ApiErrorResponse.ApiErrorCode code() {
        return ApiErrorResponse.ApiErrorCode.REMOTE_SERVICE_ERROR;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_GATEWAY;
    }

}
