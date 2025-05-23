package com.fintech.banking.common.handler;

import com.fintech.banking.common.constant.MessageConstants;
import com.fintech.banking.common.exception.CustomerNotFoundException;
import com.fintech.banking.common.exception.TransactionNotFoundException;
import com.fintech.banking.common.service.LocalizedMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LocalizedMessageService messageService;

    public GlobalExceptionHandler(LocalizedMessageService messageService) {
        this.messageService = messageService;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handeCustomerNotFoundException(RuntimeException ex) {

        String errorMessage = messageService.getMessage(MessageConstants.RUNTIME_EXCEPTION);
        log.error(errorMessage, ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> transactionNotFoundException(RuntimeException ex) {

        String errorMessage = messageService.getMessage(MessageConstants.RUNTIME_EXCEPTION);
        log.error(errorMessage, ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {

        String errorMessage = messageService.getMessage(MessageConstants.RUNTIME_EXCEPTION);
        log.error(errorMessage, ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException ex) {
        log.error("NoSuchElementException occurred", ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException occurred", ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid type for parameter: " + ex.getName());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockingFailureException ex) {
        log.error("OptimisticLockingFailureException occurred", ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Exception occurred", ex);  // ✅ logs stacktrace
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
