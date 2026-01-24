package com.amit.mybankapp.commons.dto.advice;

import java.time.LocalDateTime;

public record ApiErrorResponse(String message,
                               String timestamp,
                               String path) {

    public ApiErrorResponse(String message, String path) {
        this(message, LocalDateTime.now().toString(), path);
    }

}
