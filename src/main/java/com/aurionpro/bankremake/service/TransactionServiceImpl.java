package com.aurionpro.bankremake.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankremake.dto.DepositRequestDto;
import com.aurionpro.bankremake.dto.TransactionResponseDto;
import com.aurionpro.bankremake.dto.TransferRequestDto;
import com.aurionpro.bankremake.dto.WithdrawalRequestDto;
import com.aurionpro.bankremake.entity.BankAccount;
import com.aurionpro.bankremake.entity.Transaction;
import com.aurionpro.bankremake.enums.AccountStatus;
import com.aurionpro.bankremake.enums.TransactionType;
import com.aurionpro.bankremake.exception.AccountNotFoundException;
import com.aurionpro.bankremake.exception.BusinessException;
import com.aurionpro.bankremake.exception.InsufficientBalanceException;
import com.aurionpro.bankremake.repository.BankAccountRepository;
import com.aurionpro.bankremake.repository.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public TransactionResponseDto transfer(TransferRequestDto requestDto) {
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(requestDto.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(requestDto.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));
        
        validateActiveAccount(fromAccount, "Source account is not active");
        validateActiveAccount(toAccount, "Destination account is not active");
        
        if (fromAccount.getBalance().compareTo(requestDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(requestDto.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(requestDto.getAmount()));
        
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setDescription(requestDto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        TransactionResponseDto responseDto = modelMapper.map(savedTransaction, TransactionResponseDto.class);
        responseDto.setFromAccountNumber(fromAccount.getAccountNumber());
        responseDto.setToAccountNumber(toAccount.getAccountNumber());
        
        return responseDto;
    }
    
    @Override
    public TransactionResponseDto deposit(DepositRequestDto requestDto) {
        BankAccount account = bankAccountRepository.findById(requestDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        validateActiveAccount(account, "Account is not active");
        
        account.setBalance(account.getBalance().add(requestDto.getAmount()));
        bankAccountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setToAccount(account);
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setDescription(requestDto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        TransactionResponseDto responseDto = modelMapper.map(savedTransaction, TransactionResponseDto.class);
        responseDto.setToAccountNumber(account.getAccountNumber());
        
        return responseDto;
    }
    
    @Override
    public TransactionResponseDto withdraw(WithdrawalRequestDto requestDto) {
        BankAccount account = bankAccountRepository.findById(requestDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        validateActiveAccount(account, "Account is not active");
        
        if (account.getBalance().compareTo(requestDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        
        account.setBalance(account.getBalance().subtract(requestDto.getAmount()));
        bankAccountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account);
        transaction.setAmount(requestDto.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setDescription(requestDto.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        TransactionResponseDto responseDto = modelMapper.map(savedTransaction, TransactionResponseDto.class);
        responseDto.setFromAccountNumber(account.getAccountNumber());
        
        return responseDto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionHistory(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        
        return transactions.stream()
                .map(transaction -> {
                    TransactionResponseDto dto = modelMapper.map(transaction, TransactionResponseDto.class);
                    if (transaction.getFromAccount() != null) {
                        dto.setFromAccountNumber(transaction.getFromAccount().getAccountNumber());
                    }
                    if (transaction.getToAccount() != null) {
                        dto.setToAccountNumber(transaction.getToAccount().getAccountNumber());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalTransactionCount() {
        return transactionRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalTransactionVolume() {
        return transactionRepository.getTotalTransactionVolume();
    }
    
    private void validateActiveAccount(BankAccount account, String errorMessage) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException(errorMessage);
        }
    }
}