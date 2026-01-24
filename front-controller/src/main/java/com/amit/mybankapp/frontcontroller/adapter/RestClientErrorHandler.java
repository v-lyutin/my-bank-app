package com.amit.mybankapp.frontcontroller.adapter;

import com.amit.mybankapp.commons.dto.advice.ApiErrorResponse;
import com.amit.mybankapp.frontcontroller.application.exception.ExternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;

@Component
public class RestClientErrorHandler {

    private final ObjectMapper objectMapper;

    public RestClientErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ExternalServiceException toExternalServiceException(String serviceName, RestClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String responseBody = getResponseBody(exception);

        String message = buildMessage(serviceName, statusCode, responseBody);
        return new ExternalServiceException(message, exception);
    }

    private String buildMessage(String serviceName, HttpStatusCode statusCode, String responseBody) {
        ApiErrorResponse apiError = tryParseApiError(responseBody);

        if (apiError != null && apiError.message() != null && !apiError.message().isBlank()) {
            return apiError.message();
        }

        return serviceName + " is unavailable (HTTP " + statusCode.value() + ")";
    }

    private ApiErrorResponse tryParseApiError(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(body, ApiErrorResponse.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String getResponseBody(RestClientResponseException exception) {
        byte[] bytes = exception.getResponseBodyAsByteArray();
        if (bytes.length == 0) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
