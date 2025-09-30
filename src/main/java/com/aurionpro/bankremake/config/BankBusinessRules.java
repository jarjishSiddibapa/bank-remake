package com.aurionpro.bankremake.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "bank.business.rules")
public class BankBusinessRules {

	// ACCOUNT CREATION LIMITS
	// Maximum total accounts per user
	private int maxAccountsPerUser = 3;

	// Maximum savings accounts per user
	private int savingsAccountLimit = 2;

	// Maximum current accounts per user
	private int currentAccountLimit = 1;

	// ACCOUNT NUMBER GENERATION
	// Prefix for account numbers (could be bank code)
	private String accountNumberPrefix = "BANK";

	// Total length of account numbers
	private int accountNumberLength = 12;

	// TRANSACTION LIMITS
	// Single transaction limit (in rupees)
	private long singleTransactionLimit = 100000L;

	// Daily transaction limit per account
	private long dailyTransactionLimit = 500000L;

	// Amount that triggers admin review
	private long largeTransactionThreshold = 50000L;

	// SECURITY SETTINGS
	// Maximum login attempts before account lockout
	private int maxLoginAttempts = 5;

	// Account lockout duration (in minutes)
	private int lockoutDurationMinutes = 30;

}