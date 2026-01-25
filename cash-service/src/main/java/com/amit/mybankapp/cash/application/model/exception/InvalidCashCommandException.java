package com.amit.mybankapp.cash.application.model.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public final class InvalidCashCommandException extends ApiException {

    public InvalidCashCommandException(String message) {
        super(message);
    }

    @Override
    public ApiErrorResponse.ApiErrorCode code() {
        return ApiErrorResponse.ApiErrorCode.VALIDATION_ERROR;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

}
