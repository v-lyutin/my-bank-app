package com.amit.mybankapp.cash.api.error.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message,
                               String timestamp,
                               String path) {

    public ApiErrorResponse(String message, String path) {
        this(message, LocalDateTime.now().toString(), path);
    }

}
