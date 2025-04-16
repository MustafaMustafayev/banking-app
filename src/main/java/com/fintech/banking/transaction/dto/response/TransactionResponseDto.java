package com.fintech.banking.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {
    private Long id;
    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private Long customerId;
}
