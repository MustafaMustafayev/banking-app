package com.fintech.banking.common.exception;

public class CustomerNotFoundException extends BankingException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with ID: " + customerId);
    }
}
