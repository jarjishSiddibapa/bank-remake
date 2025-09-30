package com.aurionpro.bankremake.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.bankremake.dto.ApiResponseDto;
import com.aurionpro.bankremake.dto.DashboardResponseDto;
import com.aurionpro.bankremake.dto.UserResponseDto;
import com.aurionpro.bankremake.service.BankAccountService;
import com.aurionpro.bankremake.service.TransactionService;
import com.aurionpro.bankremake.service.UserService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BankAccountService bankAccountService;
    
    @Autowired
    private TransactionService transactionService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDto<DashboardResponseDto>> getDashboard() {
        try {
            // User Statistics
            long totalUsers = userService.getTotalUserCount();
            long regularUsers = userService.getRegularUserCount();
            long adminUsers = userService.getAdminUserCount();
            
            DashboardResponseDto.UserStats userStats = new DashboardResponseDto.UserStats(
                    totalUsers, regularUsers, adminUsers);
            
            // Account Statistics
            long totalAccounts = bankAccountService.getTotalAccountCount();
            long activeAccounts = bankAccountService.getActiveAccountCount();
            long pendingAccounts = bankAccountService.getPendingAccountCount();
            long inactiveAccounts = bankAccountService.getInactiveAccountCount();
            
            DashboardResponseDto.AccountStats accountStats = new DashboardResponseDto.AccountStats(
                    totalAccounts, activeAccounts, pendingAccounts, inactiveAccounts);
            
            // Transaction Statistics
            long totalTransactions = transactionService.getTotalTransactionCount();
            BigDecimal totalVolume = transactionService.getTotalTransactionVolume();
            
            DashboardResponseDto.TransactionStats transactionStats = new DashboardResponseDto.TransactionStats(
                    totalTransactions, totalVolume);
            
            DashboardResponseDto dashboard = new DashboardResponseDto(
                    userStats, accountStats, transactionStats, LocalDateTime.now());
            
            return ResponseEntity.ok(ApiResponseDto.success("Dashboard data retrieved successfully", dashboard));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
    
 // ADD THIS NEW ENDPOINT
    @PostMapping("/promote/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> promoteUserToAdmin(
            @PathVariable Long userId,
            @RequestParam Long adminId) {
        
        try {
            UserResponseDto promotedUser = userService.promoteToAdmin(userId, adminId);
            return ResponseEntity.ok(ApiResponseDto.success("User promoted to admin successfully", promotedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error(e.getMessage()));
        }
    }
}