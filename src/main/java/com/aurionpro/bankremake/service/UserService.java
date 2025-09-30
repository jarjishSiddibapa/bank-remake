package com.aurionpro.bankremake.service;

import java.util.List;
import java.util.Optional;

import com.aurionpro.bankremake.dto.UserLoginRequestDto;
import com.aurionpro.bankremake.dto.UserRegistrationRequestDto;
import com.aurionpro.bankremake.dto.UserResponseDto;

public interface UserService {
	UserResponseDto registerUser(UserRegistrationRequestDto requestDto);

	UserResponseDto authenticateUser(UserLoginRequestDto requestDto);

	Optional<UserResponseDto> findById(Long userId);

	List<UserResponseDto> getAllUsers();

	boolean isUsernameAvailable(String username);

	boolean isEmailAvailable(String email);

	long getTotalUserCount();

	long getRegularUserCount();

	long getAdminUserCount();

	UserResponseDto promoteToAdmin(Long userId, Long adminId);

}