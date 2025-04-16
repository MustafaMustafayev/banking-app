package com.fintech.banking.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreateResponseDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private BigDecimal balance;
}
