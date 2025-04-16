package com.fintech.banking.common.exception;

public class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}