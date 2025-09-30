package com.aurionpro.bankremake.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DepositRequestDto {
	@NotNull(message = "Account ID is required.")
	private Long accountId;
	
	@NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
	private BigDecimal amount;
	
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
