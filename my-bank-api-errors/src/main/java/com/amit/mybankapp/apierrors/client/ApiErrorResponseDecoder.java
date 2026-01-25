package com.amit.mybankapp.apierrors.client;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

public final class ApiErrorResponseDecoder {

    private static final String DEFAULT_MESSAGE = "Remote service error";

    private final ObjectMapper objectMapper;

    public ApiErrorResponseDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ApiErrorResponse decode(byte[] bodyBytes, int status, String pathFallback, String messageFallback) {
        return this.tryDecode(bodyBytes).orElseGet(() ->
                new ApiErrorResponse(
                        ApiErrorResponse.ApiErrorCode.REMOTE_SERVICE_ERROR,
                        (messageFallback == null || messageFallback.isBlank()) ? DEFAULT_MESSAGE : messageFallback,
                        status,
                        Instant.now(),
                        pathFallback,
                        Collections.emptyMap()
                )
        );
    }

    private Optional<ApiErrorResponse> tryDecode(byte[] bodyBytes) {
        if (bodyBytes == null || bodyBytes.length == 0) {
            return Optional.empty();
        }
        try {
            return Optional.of(this.objectMapper.readValue(bodyBytes, ApiErrorResponse.class));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

}
