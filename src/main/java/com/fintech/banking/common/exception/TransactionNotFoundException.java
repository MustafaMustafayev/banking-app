package com.fintech.banking.common.exception;

public class TransactionNotFoundException extends BankingException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}