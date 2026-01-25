package com.amit.mybankapp.apierrors.server;

import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.apierrors.server.exception.base.ResourceNotFoundException;
import com.amit.mybankapp.apierrors.server.factory.ApiErrorResponseFactory;
import com.amit.mybankapp.apierrors.server.util.ConstraintViolationFormatter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    private final ApiErrorResponseFactory apiErrorResponseFactory = new ApiErrorResponseFactory();

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorResponse body = this.apiErrorResponseFactory.build(
                ApiErrorResponse.ApiErrorCode.VALIDATION_ERROR,
                "Validation failed",
                status.value(),
                request,
                ConstraintViolationFormatter.buildDetails(exception)
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorResponse body = this.apiErrorResponseFactory.build(
                ApiErrorResponse.ApiErrorCode.RESOURCE_NOT_FOUND,
                exception.getMessage(),
                status.value(),
                request
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException apiException, HttpServletRequest request) {
        HttpStatus status = apiException.status();
        ApiErrorResponse body = this.apiErrorResponseFactory.build(
                apiException.code(),
                apiException.getMessage(),
                status.value(),
                request
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorResponse body = this.apiErrorResponseFactory.build(
                ApiErrorResponse.ApiErrorCode.INTERNAL_ERROR,
                "Unexpected error",
                status.value(),
                request
        );
        return ResponseEntity.status(status).body(body);
    }

}
