package com.aurionpro.bankremake.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aurionpro.bankremake.dto.UserLoginRequestDto;
import com.aurionpro.bankremake.dto.UserRegistrationRequestDto;
import com.aurionpro.bankremake.dto.UserResponseDto;
import com.aurionpro.bankremake.entity.User;
import com.aurionpro.bankremake.enums.UserRole;
import com.aurionpro.bankremake.exception.BusinessException;
import com.aurionpro.bankremake.exception.InvalidCredentialsException;
import com.aurionpro.bankremake.exception.UserAlreadyExistsException;
import com.aurionpro.bankremake.exception.UserNotFoundException;
import com.aurionpro.bankremake.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private AuthenticationManager authenticationManager;
	
    @Autowired
    private com.aurionpro.bankremake.security.JwtTokenProvider tokenProvider;
    
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserResponseDto registerUser(UserRegistrationRequestDto requestDto) {
		if (!requestDto.isPasswordMatching()) {
			throw new BusinessException("Passwords do not match");
		}

		if (!isUsernameAvailable(requestDto.getUsername())) {
			throw new UserAlreadyExistsException("Username already exists");
		}

		if (!isEmailAvailable(requestDto.getEmail())) {
			throw new UserAlreadyExistsException("Email already registered");
		}

		User user = modelMapper.map(requestDto, User.class);
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setRole(UserRole.ROLE_USER);
		user.setCreatedAt(LocalDateTime.now());

		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser, UserResponseDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDto authenticateUser(UserLoginRequestDto requestDto) {
		Optional<User> userOpt = userRepository.findByUsername(requestDto.getUsername());

		UserResponseDto userRes = null;
		
		if (userOpt.isEmpty()) {
			throw new InvalidCredentialsException("Invalid username or password");
		}

		User user = userOpt.get();
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid username or password");
		}
		
		try {
            Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token =  tokenProvider.generateToken(authentication);
            userRes = modelMapper.map(user, UserResponseDto.class);
            userRes.setToken(token);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
		return userRes;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserResponseDto> findById(Long userId) {
		return userRepository.findById(userId).map(user -> modelMapper.map(user, UserResponseDto.class));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUsernameAvailable(String username) {
		return !userRepository.existsByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isEmailAvailable(String email) {
		return !userRepository.existsByEmail(email);
	}
		
	@Override
	@Transactional(readOnly = true)
	public long getTotalUserCount() {
		return userRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long getRegularUserCount() {
		return userRepository.countByRole(UserRole.ROLE_ADMIN);
	}

	@Override
	@Transactional(readOnly = true)
	public long getAdminUserCount() {
		return userRepository.countByRole(UserRole.ROLE_USER);
	}

	@Override
    public UserResponseDto promoteToAdmin(Long userId, Long adminId) {
        // Find the admin performing the action
        User admin = userRepository.findById(adminId)
            .orElseThrow(() -> new UserNotFoundException("Admin user not found"));
        
        // Check if the performer is actually an admin
        if (admin.getRole() != UserRole.ROLE_ADMIN) {
            throw new BusinessException("Only admin users can promote others to admin");
        }
        
        // Find the user to be promoted
        User userToPromote = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User to promote not found"));
        
        // Check if user is already an admin
        if (userToPromote.getRole() == UserRole.ROLE_ADMIN) {
            throw new BusinessException("User is already an admin");
        }
        
        // Promote user to admin
        userToPromote.setRole(UserRole.ROLE_ADMIN);
        User promotedUser = userRepository.save(userToPromote);
        
        return modelMapper.map(promotedUser, UserResponseDto.class);
    }
}