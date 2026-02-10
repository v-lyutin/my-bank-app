package com.amit.mybankapp.apierrors.server.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException implements ApiErrorMappable {

    protected ApiException(String message) {
        super(message);
    }

    protected ApiException(String message, Throwable throwable) {
        super(message);
    }

    @Override
    public abstract ApiErrorResponse.ApiErrorCode code();

    @Override
    public abstract HttpStatus status();

}
