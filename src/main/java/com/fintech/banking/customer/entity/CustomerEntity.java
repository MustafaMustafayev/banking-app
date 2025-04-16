package com.fintech.banking.customer.entity;

import com.fintech.banking.transaction.entity.TransactionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private BigDecimal balance;

    @Version //create optimistic lock
    private Long version;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<TransactionEntity> transactions = new ArrayList<>();
}