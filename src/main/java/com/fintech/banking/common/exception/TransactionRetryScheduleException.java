package com.fintech.banking.common.exception;

public class TransactionRetryScheduleException extends RuntimeException {
    public TransactionRetryScheduleException(String message) {
        super(message);
    }
}
