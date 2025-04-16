package com.fintech.banking.common.exception;

public class TransactionOwnerNotFoundException extends RuntimeException {
    public TransactionOwnerNotFoundException(String message) {
        super(message);
    }
}
