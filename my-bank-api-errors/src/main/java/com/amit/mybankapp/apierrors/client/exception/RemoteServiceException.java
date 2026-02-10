package com.amit.mybankapp.apierrors.client.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public final class RemoteServiceException extends ApiException {

    private final ApiErrorResponse errorResponse;

    public RemoteServiceException(ApiErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }

    public ApiErrorResponse errorResponse() {
        return this.errorResponse;
    }

    @Override
    public ApiErrorResponse.ApiErrorCode code() {
        return ApiErrorResponse.ApiErrorCode.REMOTE_SERVICE_ERROR;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.valueOf(this.errorResponse.status());
    }

}
