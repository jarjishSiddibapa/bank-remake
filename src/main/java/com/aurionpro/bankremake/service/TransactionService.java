package com.aurionpro.bankremake.service;

import java.math.BigDecimal;
import java.util.List;

import com.aurionpro.bankremake.dto.DepositRequestDto;
import com.aurionpro.bankremake.dto.TransactionResponseDto;
import com.aurionpro.bankremake.dto.TransferRequestDto;
import com.aurionpro.bankremake.dto.WithdrawalRequestDto;

public interface TransactionService {
    TransactionResponseDto transfer(TransferRequestDto requestDto);
    TransactionResponseDto deposit(DepositRequestDto requestDto);
    TransactionResponseDto withdraw(WithdrawalRequestDto requestDto);
    List<TransactionResponseDto> getTransactionHistory(Long accountId);
    long getTotalTransactionCount();
    BigDecimal getTotalTransactionVolume();
}