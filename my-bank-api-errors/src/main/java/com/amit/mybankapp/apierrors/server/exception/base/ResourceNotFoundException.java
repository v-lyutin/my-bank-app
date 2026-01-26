package com.amit.mybankapp.apierrors.server.exception.base;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public ApiErrorResponse.ApiErrorCode code() {
        return ApiErrorResponse.ApiErrorCode.RESOURCE_NOT_FOUND;
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }

    public static ResourceNotFoundException forAccount(UUID customerId) {
        return new ResourceNotFoundException("Account not found for customerId=" + customerId);
    }

    public static ResourceNotFoundException forWalletOfCustomer(UUID customerId) {
        return new ResourceNotFoundException("Wallet not found for customerId=" + customerId);
    }

}
