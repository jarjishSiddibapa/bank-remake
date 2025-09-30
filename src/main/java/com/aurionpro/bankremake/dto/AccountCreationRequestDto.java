package com.aurionpro.bankremake.dto;

import com.aurionpro.bankremake.enums.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountCreationRequestDto {
	@NotNull(message = "User ID is required.")
	private Long userId;
	
	@NotNull(message = "Account type is required.")
	private AccountType accountType;
}
