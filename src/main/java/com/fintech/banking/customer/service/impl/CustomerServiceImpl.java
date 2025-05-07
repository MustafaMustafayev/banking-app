package com.fintech.banking.customer.service.impl;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;
import com.fintech.banking.customer.entity.CustomerEntity;
import com.fintech.banking.customer.mapper.CustomerMapper;
import com.fintech.banking.customer.repository.CustomerRepository;
import com.fintech.banking.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Value("${banking.customer.initial-balance}")
    private BigDecimal DEFAULT_BALANCE;

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerCreateResponseDto createCustomer(CustomerCreateRequestDto request) {
        CustomerEntity customerEntity = customerMapper.toEntity(request);
        customerEntity.setBalance(DEFAULT_BALANCE);

        customerRepository.save(customerEntity);

        return customerMapper.toDto(customerEntity);
    }
}
