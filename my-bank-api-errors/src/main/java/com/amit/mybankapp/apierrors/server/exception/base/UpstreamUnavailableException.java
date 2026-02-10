package com.amit.mybankapp.apierrors.server.exception.base;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UpstreamUnavailableException extends ApiException {

    public UpstreamUnavailableException(String message, Throwable cause) {
        super(message);
        initCause(cause);
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
