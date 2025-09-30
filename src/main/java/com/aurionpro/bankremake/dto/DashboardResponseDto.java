package com.aurionpro.bankremake.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDto {
    private UserStats userStats;
    private AccountStats accountStats;
    private TransactionStats transactionStats;
    private LocalDateTime generatedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStats {
        private long totalUsers;
        private long regularUsers;
        private long adminUsers;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountStats {
        private long totalAccounts;
        private long activeAccounts;
        private long pendingAccounts;
        private long inactiveAccounts;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionStats {
        private long totalTransactions;
        private BigDecimal totalVolume;
    }
}