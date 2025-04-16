package com.fintech.banking.transaction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionTopUpRequestDto {
    private Long customerId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @DecimalMin(value = "0.0001", inclusive = true, message = "Amount must be at least 0.0001")
    //assume 0.0001 is the smallest amount that allowed to process
    private BigDecimal amount;
}
