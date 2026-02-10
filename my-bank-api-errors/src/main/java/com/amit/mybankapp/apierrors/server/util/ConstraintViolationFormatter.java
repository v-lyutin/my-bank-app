package com.amit.mybankapp.apierrors.server.util;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ConstraintViolationFormatter {

    public static final String FIELD_ERRORS_KEY = "fieldErrors";

    public static Map<String, Object> buildDetails(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Invalid value"),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        return Map.of(FIELD_ERRORS_KEY, fieldErrors);
    }

    private ConstraintViolationFormatter() {
        throw new UnsupportedOperationException();
    }

}
