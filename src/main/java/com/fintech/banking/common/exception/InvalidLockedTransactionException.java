package com.fintech.banking.common.exception;

public class InvalidLockedTransactionException extends RuntimeException {
    public InvalidLockedTransactionException(String message) {
        super(message);
    }
}
