package com.aurionpro.bankremake.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransferRequestDto {
	@NotBlank(message = "From account number is required.")
	private String fromAccountNumber;;
	
	@NotBlank(message = "To account number is required.")
	private String toAccountNumber;
	
	@NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
	private BigDecimal amount;
	
    @Size(max = 255, message = "Description cannot exceed 255 characters")	
	private String description;
}
