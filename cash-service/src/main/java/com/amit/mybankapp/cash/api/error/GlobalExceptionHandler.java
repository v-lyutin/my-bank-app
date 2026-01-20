package com.amit.mybankapp.cash.api.error;

import com.amit.mybankapp.cash.api.error.dto.ApiErrorResponse;
import com.amit.mybankapp.cash.application.exception.CashOperationExecutionException;
import com.amit.mybankapp.cash.application.model.exception.InvalidCashCommandException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception,
                                                                      HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .distinct()
                .toList()
                .toString();

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(value = InvalidCashCommandException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCashCommandException(InvalidCashCommandException exception,
                                                                              HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(HttpClientErrorException.NotFound exception,
                                                                    HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpClientErrorException(HttpClientErrorException exception,
                                                                           HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(exception.getStatusCode()).body(apiErrorResponse);
    }

    @ExceptionHandler(value = HttpServerErrorException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpServerErrorException(HttpServerErrorException exception,
                                                                           HttpServletRequest request) {
        LOGGER.warn("Upstream service error on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("upstream service error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiErrorResponse);
    }

    @ExceptionHandler(value = ResourceAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceAccessException(ResourceAccessException exception,
                                                                          HttpServletRequest request) {
        LOGGER.warn("Upstream connection error on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("upstream connection error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiErrorResponse);
    }

    @ExceptionHandler(value = CashOperationExecutionException.class)
    public ResponseEntity<ApiErrorResponse> handleCashOperationExecutionException(CashOperationExecutionException exception,
                                                                                  HttpServletRequest request) {
        LOGGER.warn("Cash operation execution failed on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("cash operation execution failed", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(apiErrorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception,
                                                                   HttpServletRequest request) {
        LOGGER.error("Unhandled exception on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("internal server error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

}
