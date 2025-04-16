package com.fintech.banking.transaction;

import com.fintech.banking.customer.entity.CustomerEntity;

import java.math.BigDecimal;

public class DummyDataProvider {

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "test name";
    private static final String CUSTOMER_SURNAME = "test surname";

    public static CustomerEntity createCustomerEntity(BigDecimal initialBalance) {
        return CustomerEntity.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .balance(initialBalance)
                .build();
    }
}
