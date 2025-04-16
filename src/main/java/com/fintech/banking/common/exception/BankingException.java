package com.fintech.banking.common.exception;

public class BankingException extends RuntimeException {
    public BankingException(String message) {
        super(message);
    }
}