package com.fintech.banking.customer.dto.request;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@Builder
public class CustomerCreateRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @Past(message = "Birthdate must be in the past")
    @NotNull(message = "Birthdate is required")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "Phone number must be in a valid format")
    private String phoneNumber;
}
