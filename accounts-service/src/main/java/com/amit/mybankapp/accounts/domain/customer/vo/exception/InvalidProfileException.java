package com.amit.mybankapp.accounts.domain.customer.vo.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidProfileException extends ApiException {

    public InvalidProfileException(String message) {
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
