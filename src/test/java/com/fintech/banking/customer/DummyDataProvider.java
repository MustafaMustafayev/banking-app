package com.fintech.banking.customer;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;
import com.fintech.banking.customer.entity.CustomerEntity;

import java.math.BigDecimal;

public class DummyDataProvider {

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "test name";
    private static final String CUSTOMER_SURNAME = "test surname";
    private static final String CUSTOMER_PHONE_NUMBER = "+994508556876";
    private static final BigDecimal CUSTOMER_BALANCE = BigDecimal.valueOf(100.0);

    public static CustomerEntity createCustomerEntity() {
        return CustomerEntity.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .phoneNumber(CUSTOMER_PHONE_NUMBER)
                .balance(CUSTOMER_BALANCE)
                .build();
    }

    public static CustomerCreateRequestDto createCustomerCreateRequestDto() {

        return CustomerCreateRequestDto.builder()
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .phoneNumber(CUSTOMER_PHONE_NUMBER)
                .build();
    }

    public static CustomerCreateResponseDto createCustomerCreateResponseDto() {
        return CustomerCreateResponseDto.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .phoneNumber(CUSTOMER_PHONE_NUMBER)
                .balance(CUSTOMER_BALANCE)
                .build();
    }
}