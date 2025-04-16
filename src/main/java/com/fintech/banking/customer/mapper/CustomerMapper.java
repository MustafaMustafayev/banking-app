package com.fintech.banking.customer.mapper;

import com.fintech.banking.customer.dto.request.CustomerCreateRequestDto;
import com.fintech.banking.customer.dto.response.CustomerCreateResponseDto;
import com.fintech.banking.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerEntity toEntity (CustomerCreateRequestDto source);

    CustomerCreateResponseDto toDto(CustomerEntity source);
}
