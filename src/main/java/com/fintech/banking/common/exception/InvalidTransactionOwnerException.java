package com.fintech.banking.common.exception;

public class InvalidTransactionOwnerException extends BankingException {
    public InvalidTransactionOwnerException(String message) {
        super(message);
    }
}
