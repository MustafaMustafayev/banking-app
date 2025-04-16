package com.fintech.banking.transaction.mapper;

import com.fintech.banking.transaction.dto.response.TransactionResponseDto;
import com.fintech.banking.transaction.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TransactionMapper {
    @Mapping(source = "customer.id", target = "customerId")
    TransactionResponseDto toDto(TransactionEntity transaction);
}
