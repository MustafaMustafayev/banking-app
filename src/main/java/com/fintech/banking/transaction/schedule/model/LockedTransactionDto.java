package com.fintech.banking.transaction.schedule.model;

import com.fintech.banking.transaction.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.OptionalLong;

@Data
@Builder
public class LockedTransactionDto {
    private Long customerId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private OptionalLong transactionId;
}
