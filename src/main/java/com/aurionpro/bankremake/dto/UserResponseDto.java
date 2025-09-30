package com.aurionpro.bankremake.dto;

import java.time.LocalDateTime;

import com.aurionpro.bankremake.enums.UserRole;

import lombok.Data;

@Data
public class UserResponseDto {
	private Long userId;
	private String username;
	private String fullName;
	private String email;
	private String phone;
	private UserRole role;
	private String token;
	private LocalDateTime createdAt;
}
