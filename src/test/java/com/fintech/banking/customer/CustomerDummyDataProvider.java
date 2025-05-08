package com.fintech.banking.customer;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;
import com.fintech.banking.customer.entity.CustomerEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerDummyDataProvider {

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

    /**
     * Creates a valid customer request DTO for testing.
     *
     * @return A valid CustomerCreateRequestDto
     */
    public static CustomerCreateRequestDto createValidCustomerRequest() {
        return CustomerCreateRequestDto.builder()
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("+1234567890")
                .build();
    }

    /**
     * Creates a customer request DTO with blank name for validation testing.
     *
     * @return CustomerCreateRequestDto with blank name
     */
    public static CustomerCreateRequestDto createCustomerRequestWithBlankName() {
        return CustomerCreateRequestDto.builder()
                .name("")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("+1234567890")
                .build();
    }

    /**
     * Creates a customer request DTO with blank surname for validation testing.
     *
     * @return CustomerCreateRequestDto with blank surname
     */
    public static CustomerCreateRequestDto createCustomerRequestWithBlankSurname() {
        return CustomerCreateRequestDto.builder()
                .name("John")
                .surname("")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("+1234567890")
                .build();
    }

    /**
     * Creates a customer request DTO with future birth date for validation testing.
     *
     * @return CustomerCreateRequestDto with future birth date
     */
    public static CustomerCreateRequestDto createCustomerRequestWithFutureBirthDate() {
        return CustomerCreateRequestDto.builder()
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.now().plusDays(1))
                .phoneNumber("+1234567890")
                .build();
    }

    /**
     * Creates a customer request DTO with invalid phone number for validation testing.
     *
     * @return CustomerCreateRequestDto with invalid phone number
     */
    public static CustomerCreateRequestDto createCustomerRequestWithInvalidPhone() {
        return CustomerCreateRequestDto.builder()
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .phoneNumber("invalid-phone")
                .build();
    }
}