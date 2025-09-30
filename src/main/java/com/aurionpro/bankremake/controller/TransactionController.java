package com.aurionpro.bankremake.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.bankremake.dto.ApiResponseDto;
import com.aurionpro.bankremake.dto.DepositRequestDto;
import com.aurionpro.bankremake.dto.TransactionResponseDto;
import com.aurionpro.bankremake.dto.TransferRequestDto;
import com.aurionpro.bankremake.dto.WithdrawalRequestDto;
import com.aurionpro.bankremake.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponseDto<TransactionResponseDto>> transfer(
            @Valid @RequestBody TransferRequestDto requestDto,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Validation failed"));
        }
        
        try {
            TransactionResponseDto transaction = transactionService.transfer(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("Transfer completed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponseDto<TransactionResponseDto>> deposit(
            @Valid @RequestBody DepositRequestDto requestDto,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Validation failed"));
        }
        
        try {
            TransactionResponseDto transaction = transactionService.deposit(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("Deposit completed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponseDto<TransactionResponseDto>> withdraw(
            @Valid @RequestBody WithdrawalRequestDto requestDto,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Validation failed"));
        }
        
        try {
            TransactionResponseDto transaction = transactionService.withdraw(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDto.success("Withdrawal completed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
    
    @GetMapping("/history/{accountId}")
    public ResponseEntity<ApiResponseDto<List<TransactionResponseDto>>> getTransactionHistory(@PathVariable Long accountId) {
        try {
            List<TransactionResponseDto> transactions = transactionService.getTransactionHistory(accountId);
            return ResponseEntity.ok(ApiResponseDto.success("Transaction history retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
}