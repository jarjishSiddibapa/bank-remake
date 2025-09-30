package com.aurionpro.bankremake.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankremake.dto.AccountCreationRequestDto;
import com.aurionpro.bankremake.dto.AccountResponseDto;
import com.aurionpro.bankremake.entity.BankAccount;
import com.aurionpro.bankremake.entity.User;
import com.aurionpro.bankremake.enums.AccountStatus;
import com.aurionpro.bankremake.enums.AccountType;
import com.aurionpro.bankremake.enums.UserRole;
import com.aurionpro.bankremake.exception.AccountNotFoundException;
import com.aurionpro.bankremake.exception.BusinessException;
import com.aurionpro.bankremake.exception.UserNotFoundException;
import com.aurionpro.bankremake.repository.BankAccountRepository;
import com.aurionpro.bankremake.repository.UserRepository;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public AccountResponseDto createAccount(AccountCreationRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setAccountType(requestDto.getAccountType());
        account.setAccountNumber(generateAccountNumber(requestDto.getAccountType()));
        account.setStatus(AccountStatus.PENDING);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        
        BankAccount savedAccount = bankAccountRepository.save(account);
        
        AccountResponseDto responseDto = modelMapper.map(savedAccount, AccountResponseDto.class);
        responseDto.setOwnerName(user.getFullName());
        return responseDto;
    }
    
    @Override
    public AccountResponseDto approveAccount(Long accountId, Long adminId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        
        if (admin.getRole() != UserRole.ROLE_ADMIN) {
            throw new BusinessException("Only admin can approve accounts");
        }
        
        if (account.getStatus() != AccountStatus.PENDING) {
            throw new BusinessException("Only pending accounts can be approved");
        }
        
        account.setStatus(AccountStatus.ACTIVE);
        account.setApprovedAt(LocalDateTime.now());
        
        BankAccount savedAccount = bankAccountRepository.save(account);
        
        AccountResponseDto responseDto = modelMapper.map(savedAccount, AccountResponseDto.class);
        responseDto.setOwnerName(savedAccount.getUser().getFullName());
        return responseDto;
    }
    
    @Override
    public AccountResponseDto rejectAccount(Long accountId, Long adminId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        
        if (admin.getRole() != UserRole.ROLE_ADMIN) {
            throw new BusinessException("Only admin can reject accounts");
        }
        
        if (account.getStatus() != AccountStatus.PENDING) {
            throw new BusinessException("Only pending accounts can be rejected");
        }
        
        account.setStatus(AccountStatus.INACTIVE);
        
        BankAccount savedAccount = bankAccountRepository.save(account);
        
        AccountResponseDto responseDto = modelMapper.map(savedAccount, AccountResponseDto.class);
        responseDto.setOwnerName(savedAccount.getUser().getFullName());
        return responseDto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> findById(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .map(account -> {
                    AccountResponseDto dto = modelMapper.map(account, AccountResponseDto.class);
                    dto.setOwnerName(account.getUser().getFullName());
                    return dto;
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountResponseDto> findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    AccountResponseDto dto = modelMapper.map(account, AccountResponseDto.class);
                    dto.setOwnerName(account.getUser().getFullName());
                    return dto;
                });
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findByUserId(Long userId) {
        return bankAccountRepository.findByUserUserId(userId).stream()
                .map(account -> {
                    AccountResponseDto dto = modelMapper.map(account, AccountResponseDto.class);
                    dto.setOwnerName(account.getUser().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findPendingAccounts() {
        return bankAccountRepository.findByStatus(AccountStatus.PENDING).stream()
                .map(account -> {
                    AccountResponseDto dto = modelMapper.map(account, AccountResponseDto.class);
                    dto.setOwnerName(account.getUser().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getTotalAccountCount() {
        return bankAccountRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getActiveAccountCount() {
        return bankAccountRepository.countByStatus(AccountStatus.ACTIVE);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPendingAccountCount() {
        return bankAccountRepository.countByStatus(AccountStatus.PENDING);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getInactiveAccountCount() {
        return bankAccountRepository.countByStatus(AccountStatus.INACTIVE);
    }
    
    private String generateAccountNumber(AccountType accountType) {
        String prefix = switch (accountType) {
            case SAVINGS -> "SB";
            case CURRENT -> "CA";
            case FIXED_DEPOSIT -> "FD";
        };
        
        String accountNumber;
        do {
            long randomNumber = ThreadLocalRandom.current().nextLong(10000000L, 100000000L);
            accountNumber = prefix + randomNumber;
        } while (bankAccountRepository.existsByAccountNumber(accountNumber));
        
        return accountNumber;
    }
}