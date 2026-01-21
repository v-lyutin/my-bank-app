package com.amit.mybankapp.transfer.api.error.dto;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message,
                               String timestamp,
                               String path) {

    public ApiErrorResponse(String message, String path) {
        this(message, LocalDateTime.now().toString(), path);
    }

}
