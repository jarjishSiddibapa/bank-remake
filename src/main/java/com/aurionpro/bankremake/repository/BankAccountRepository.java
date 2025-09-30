package com.aurionpro.bankremake.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aurionpro.bankremake.entity.BankAccount;
import com.aurionpro.bankremake.enums.AccountStatus;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByUserUserId(Long userId);
    List<BankAccount> findByStatus(AccountStatus status);
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT COUNT(ba) FROM BankAccount ba WHERE ba.status = ?1")
    long countByStatus(AccountStatus status);
}