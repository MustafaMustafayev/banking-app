package com.fintech.banking.common.exception;

public class InvalidTransactionTypeException extends BankingException {
    public InvalidTransactionTypeException(String message) {
        super(message);
    }
}
