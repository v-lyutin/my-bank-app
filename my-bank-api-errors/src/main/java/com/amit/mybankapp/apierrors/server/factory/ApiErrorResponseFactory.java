package com.amit.mybankapp.apierrors.server.factory;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public final class ApiErrorResponseFactory {

    public ApiErrorResponse build(ApiErrorResponse.ApiErrorCode code,
                                  String message,
                                  int status,
                                  HttpServletRequest request,
                                  Map<String, Object> details) {
        return ApiErrorResponse.of(code, message, status, request.getRequestURI(), details);
    }

    public ApiErrorResponse build(ApiErrorResponse.ApiErrorCode code,
                                  String message,
                                  int status,
                                  HttpServletRequest request) {
        return ApiErrorResponse.of(code, message, status, request.getRequestURI());
    }

}
