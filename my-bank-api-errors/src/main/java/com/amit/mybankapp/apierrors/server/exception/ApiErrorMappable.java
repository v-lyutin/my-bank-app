package com.amit.mybankapp.apierrors.server.exception;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import org.springframework.http.HttpStatus;

public interface ApiErrorMappable {

    ApiErrorResponse.ApiErrorCode code();

    HttpStatus status();

    default String message() {
        return null;

}
