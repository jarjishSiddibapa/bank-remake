package com.aurionpro.bankremake.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.bankremake.enums.AccountStatus;
import com.aurionpro.bankremake.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountType accountType;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AccountStatus status;

    @Column(nullable = false, precision = 15, scale = 2)
    @NotNull @DecimalMin("0.00")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;
}
