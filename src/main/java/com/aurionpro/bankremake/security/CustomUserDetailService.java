package com.aurionpro.bankremake.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aurionpro.bankremake.enums.UserRole;
import com.aurionpro.bankremake.exception.UserNotFoundException;
import com.aurionpro.bankremake.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		com.aurionpro.bankremake.entity.User user = userRepo.findByUsername(username).
				orElseThrow(()->new UserNotFoundException("User not found"));
		
		UserRole role = user.getRole();
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
		
		Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(authority);	
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
