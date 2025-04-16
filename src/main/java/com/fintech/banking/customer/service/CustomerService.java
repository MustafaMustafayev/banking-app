package com.fintech.banking.customer.service;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;

public interface CustomerService {
    CustomerCreateResponseDto createCustomer(CustomerCreateRequestDto request);
}
