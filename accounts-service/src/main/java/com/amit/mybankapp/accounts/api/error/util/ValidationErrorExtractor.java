package com.amit.mybankapp.accounts.api.error.util;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public final class ValidationErrorExtractor {

    public static List<ValidationError> extractValidationErrors(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ValidationErrorExtractor::mapObjectError)
                .toList();
    }

    private static ValidationError mapObjectError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return new ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }
        return new ValidationError(
                error.getObjectName(),
                error.getDefaultMessage()
        );
    }

    private ValidationErrorExtractor() {
        throw new UnsupportedOperationException();
    }

    public record ValidationError(String field, String message) {}

}
