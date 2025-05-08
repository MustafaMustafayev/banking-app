package com.fintech.banking.customer;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;
import com.fintech.banking.customer.entity.CustomerEntity;
import com.fintech.banking.customer.mapper.CustomerMapper;
import com.fintech.banking.customer.repository.CustomerRepository;
import com.fintech.banking.customer.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerMapper customerMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCustomer_shouldMapAndSaveAndReturnResponse() {

        CustomerCreateRequestDto request = CustomerDummyDataProvider.createCustomerCreateRequestDto();
        CustomerEntity savedCustomer = CustomerDummyDataProvider.createCustomerEntity();

        when(customerMapper.toEntity(request))
                .thenReturn(savedCustomer);

        CustomerCreateResponseDto responseDto = CustomerDummyDataProvider.createCustomerCreateResponseDto();

        when(customerMapper.toDto(savedCustomer))
                .thenReturn(responseDto);

        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedCustomer);

        CustomerCreateResponseDto result = customerService.createCustomer(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getSurname(), result.getSurname());
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber());
    }
}