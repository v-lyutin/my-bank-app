package com.amit.mybankapp.accounts.api.error;

import com.amit.mybankapp.accounts.api.error.dto.ApiErrorResponse;
import com.amit.mybankapp.accounts.api.error.util.ValidationErrorExtractor;
import com.amit.mybankapp.accounts.application.common.exception.ResourceNotFoundException;
import com.amit.mybankapp.accounts.application.transfer.exception.InvalidTransferException;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InsufficientFundsException;
import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidLoginException;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InvalidMoneyException;
import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidProfileException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<ValidationErrorExtractor.ValidationError> errors = ValidationErrorExtractor.extractValidationErrors(exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "Validation failed",
                request.getRequestURI(),
                errors
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = ConversionFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleConversionFailedException(ConversionFailedException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                "Invalid request parameter",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiErrorResponse);
    }

    @ExceptionHandler(value = {
            InvalidProfileException.class,
            InvalidMoneyException.class,
            InvalidLoginException.class
    })
    public ResponseEntity<ApiErrorResponse> handleDomainValidationExceptions(RuntimeException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(value = InvalidTransferException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTransferException(InvalidTransferException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    @ExceptionHandler(value = InsufficientFundsException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientFundsException(InsufficientFundsException exception, HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
        LOGGER.error("Unhandled exception on {}", request.getRequestURI(), exception);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("internal server error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

}
