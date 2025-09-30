package com.aurionpro.bankremake.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.bankremake.enums.TransactionType;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private BankAccount fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private BankAccount toAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull @DecimalMin("0.01")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionType transactionType;

    private String description;

    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
