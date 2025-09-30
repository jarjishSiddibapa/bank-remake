package com.aurionpro.bankremake.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.bankremake.dto.AccountCreationRequestDto;
import com.aurionpro.bankremake.dto.AccountResponseDto;
import com.aurionpro.bankremake.dto.ApiResponseDto;
import com.aurionpro.bankremake.service.BankAccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class BankAccountController {

	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping
	public ResponseEntity<ApiResponseDto<AccountResponseDto>> createAccount(
			@Valid @RequestBody AccountCreationRequestDto requestDto, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error("Validation failed"));
		}

		try {
			AccountResponseDto account = bankAccountService.createAccount(requestDto);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ApiResponseDto.success("Account created successfully", account));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@PostMapping("/{accountId}/approve")
	public ResponseEntity<ApiResponseDto<AccountResponseDto>> approveAccount(@PathVariable Long accountId,
			@RequestParam Long adminId) {

		try {
			AccountResponseDto account = bankAccountService.approveAccount(accountId, adminId);
			return ResponseEntity.ok(ApiResponseDto.success("Account approved successfully", account));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@PostMapping("/{accountId}/reject")
	public ResponseEntity<ApiResponseDto<AccountResponseDto>> rejectAccount(@PathVariable Long accountId,
			@RequestParam Long adminId) {

		try {
			AccountResponseDto account = bankAccountService.rejectAccount(accountId, adminId);
			return ResponseEntity.ok(ApiResponseDto.success("Account rejected successfully", account));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccountById(@PathVariable Long accountId) {
		try {
			Optional<AccountResponseDto> account = bankAccountService.findById(accountId);
			if (account.isPresent()) {
				return ResponseEntity.ok(ApiResponseDto.success("Account found", account.get()));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@GetMapping("/account-number/{accountNumber}")
	public ResponseEntity<ApiResponseDto<AccountResponseDto>> getAccountByNumber(@PathVariable String accountNumber) {
		try {
			Optional<AccountResponseDto> account = bankAccountService.findByAccountNumber(accountNumber);
			if (account.isPresent()) {
				return ResponseEntity.ok(ApiResponseDto.success("Account found", account.get()));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponseDto<List<AccountResponseDto>>> getUserAccounts(@PathVariable Long userId) {
		try {
			List<AccountResponseDto> accounts = bankAccountService.findByUserId(userId);
			return ResponseEntity.ok(ApiResponseDto.success("Accounts retrieved successfully", accounts));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}

	@GetMapping("/pending")
	public ResponseEntity<ApiResponseDto<List<AccountResponseDto>>> getPendingAccounts() {
		try {
			List<AccountResponseDto> accounts = bankAccountService.findPendingAccounts();
			return ResponseEntity.ok(ApiResponseDto.success("Pending accounts retrieved successfully", accounts));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
		}
	}
}