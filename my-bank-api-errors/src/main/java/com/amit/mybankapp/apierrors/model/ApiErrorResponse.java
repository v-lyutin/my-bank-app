package com.amit.mybankapp.apierrors.model;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        ApiErrorCode code,
        String message,
        int status,
        Instant timestamp,
        String path,
        Map<String, Object> details) {

    public static ApiErrorResponse of(ApiErrorCode code, String message, int status, String path) {
        return new ApiErrorResponse(code, message, status, Instant.now(), path, null);
    }

    public static ApiErrorResponse of(ApiErrorCode code, String message, int status, String path, Map<String, Object> details) {
        return new ApiErrorResponse(code, message, status, Instant.now(), path, details);
    }

    public enum ApiErrorCode {

        VALIDATION_ERROR,

        RESOURCE_NOT_FOUND,

        CONFLICT,

        UNAUTHORIZED,

        FORBIDDEN,

        INTERNAL_ERROR,

        REMOTE_SERVICE_ERROR

    }

}
