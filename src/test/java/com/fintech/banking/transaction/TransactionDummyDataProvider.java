package com.fintech.banking.transaction;

import com.fintech.banking.customer.entity.CustomerEntity;
import com.fintech.banking.transaction.dto.request.TransactionPurchaseRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionRefundRequestDto;
import com.fintech.banking.transaction.dto.request.TransactionTopUpRequestDto;
import com.fintech.banking.transaction.entity.TransactionEntity;
import com.fintech.banking.transaction.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionDummyDataProvider {

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "test name";
    private static final String CUSTOMER_SURNAME = "test surname";
    private static final String CUSTOMER_PHONE_NUMBER = "+994511111111";
    private static final BigDecimal CUSTOMER_BALANCE = BigDecimal.valueOf(100.0);

    public static CustomerEntity createCustomer() {
        return CustomerEntity.builder()
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .phoneNumber(CUSTOMER_PHONE_NUMBER)
                .balance(CUSTOMER_BALANCE)
                .birthDate(LocalDate.of(1996,12,4))
                .build();
    }

    public static CustomerEntity createCustomerWithZeroBalance() {
        return CustomerEntity.builder()
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .phoneNumber(CUSTOMER_PHONE_NUMBER)
                .balance(BigDecimal.valueOf(0))
                .birthDate(LocalDate.of(1996,12,4))
                .build();
    }


    public static CustomerEntity createCustomerEntity(BigDecimal initialBalance) {
        return CustomerEntity.builder()
                .id(CUSTOMER_ID)
                .name(CUSTOMER_NAME)
                .surname(CUSTOMER_SURNAME)
                .balance(initialBalance)
                .build();
    }

    public static TransactionEntity createTransaction(CustomerEntity customer, BigDecimal amount, TransactionType type) {
        return TransactionEntity.builder()
                .customer(customer)
                .amount(amount)
                .type(type)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static TransactionTopUpRequestDto createTopUpRequest(Long customerId, BigDecimal amount) {
        TransactionTopUpRequestDto dto = new TransactionTopUpRequestDto();
        dto.setCustomerId(customerId);
        dto.setAmount(amount);
        return dto;
    }

    public static TransactionPurchaseRequestDto createPurchaseRequest(Long customerId, BigDecimal amount) {
        TransactionPurchaseRequestDto dto = new TransactionPurchaseRequestDto();
        dto.setCustomerId(customerId);
        dto.setAmount(amount);
        return dto;
    }

    public static TransactionRefundRequestDto createRefundRequest(Long customerId, Long originalTransactionId, BigDecimal amount) {
        TransactionRefundRequestDto dto = new TransactionRefundRequestDto();
        dto.setCustomerId(customerId);
        dto.setOriginalTransactionId(originalTransactionId);
        dto.setAmount(amount);
        return dto;
    }

    public static String generateIdempotencyKey() {
        return UUID.randomUUID().toString();
    }
}
