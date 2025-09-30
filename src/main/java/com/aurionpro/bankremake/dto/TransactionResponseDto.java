package com.aurionpro.bankremake.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.bankremake.enums.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;
    private LocalDateTime transactionDate;
}
