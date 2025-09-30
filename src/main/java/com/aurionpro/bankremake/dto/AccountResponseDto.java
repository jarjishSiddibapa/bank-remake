package com.aurionpro.bankremake.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.bankremake.enums.AccountStatus;
import com.aurionpro.bankremake.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
	private Long accountId;
	private String accountNumber;
	private AccountType accountType;
	private AccountStatus status;
	private BigDecimal balance;
	private String ownerName;
	private LocalDateTime createdAt;
	private LocalDateTime approvedAt;
}
