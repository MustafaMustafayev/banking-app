package com.fintech.banking.common.exception;

public class GreaterRefundAmountThanTransactionException extends RuntimeException {
    public GreaterRefundAmountThanTransactionException(String message) {
        super(message);
    }
}
