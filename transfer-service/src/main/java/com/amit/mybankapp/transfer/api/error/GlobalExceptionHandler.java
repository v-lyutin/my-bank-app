package com.amit.mybankapp.transfer.api.error;

import com.amit.mybankapp.transfer.api.error.dto.ApiErrorResponse;
import com.amit.mybankapp.transfer.application.exception.TransferExecutionException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(value = TransferExecutionException.class)
    public ResponseEntity<ApiErrorResponse> handleTransferExecutionException(TransferExecutionException exception, HttpServletRequest request) {
        LOGGER.error("Transfer execution failed on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("transfer execution failed", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiErrorResponse);
    }

    @ExceptionHandler(value = RestClientResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleRestClientResponseException(RestClientResponseException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());

        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }

        if (status.is5xxServerError()) {
            ApiErrorResponse response = new ApiErrorResponse("upstream service error", request.getRequestURI());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
        }

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(apiErrorResponse);
    }

    @ExceptionHandler(value = ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAccessException(ResourceAccessException exception, HttpServletRequest request) {
        LOGGER.warn("Upstream connection error on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("upstream connection error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiErrorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
        LOGGER.error("Unhandled exception on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("internal server error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

}
