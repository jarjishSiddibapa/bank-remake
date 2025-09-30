package com.aurionpro.bankremake.service;

import java.util.List;
import java.util.Optional;

import com.aurionpro.bankremake.dto.AccountCreationRequestDto;
import com.aurionpro.bankremake.dto.AccountResponseDto;

public interface BankAccountService {
    AccountResponseDto createAccount(AccountCreationRequestDto requestDto);
    AccountResponseDto approveAccount(Long accountId, Long adminId);
    AccountResponseDto rejectAccount(Long accountId, Long adminId);
    Optional<AccountResponseDto> findById(Long accountId);
    Optional<AccountResponseDto> findByAccountNumber(String accountNumber);
    List<AccountResponseDto> findByUserId(Long userId);
    List<AccountResponseDto> findPendingAccounts();
    long getTotalAccountCount();
    long getActiveAccountCount();
    long getPendingAccountCount();
    long getInactiveAccountCount();
}