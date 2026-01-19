package com.amit.mybankapp.accounts.api.error.dto;

import com.amit.mybankapp.accounts.api.error.util.ValidationErrorExtractor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ApiErrorResponse(String message,
                               String timestamp,
                               String path,
                               @JsonInclude(JsonInclude.Include.NON_EMPTY)
                               List<ValidationErrorExtractor.ValidationError> errors) {

    public ApiErrorResponse(String message, String path) {
        this(message, LocalDateTime.now().toString(), path, Collections.emptyList());
    }

    public ApiErrorResponse(String message, String path, List<ValidationErrorExtractor.ValidationError> errors) {
        this(message, LocalDateTime.now().toString(), path, errors);
    }

}