package com.amit.mybankapp.accounts.domain.wallet.vo.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

public final class InsufficientFundsException extends ApiException {

    public InsufficientFundsException() {
        super("insufficient funds");
    }

    @Override
    public ApiErrorResponse.ApiErrorCode code() {
        return ApiErrorResponse.ApiErrorCode.CONFLICT;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }

}
