package com.amit.mybankapp.cash.application.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class CashOperationExecutionException extends ApiException {

    private final UUID operationId;

    public CashOperationExecutionException(UUID operationId, Throwable cause) {
        super("Cash operationType " + operationId + " execution failed", cause);
        this.operationId = operationId;
    }

    public UUID operationId() {
        return this.operationId;
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
