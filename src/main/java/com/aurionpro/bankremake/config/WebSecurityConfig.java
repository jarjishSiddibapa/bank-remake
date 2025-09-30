package com.aurionpro.bankremake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final PasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll()
				.requestMatchers("/admin/**").hasRole("ROLE_ADMIN")).formLogin(Customizer.withDefaults());
		return httpSecurity.build();
	}

	@Bean
	UserDetailsService userDetailService() {
		UserDetails user1 = User.withUsername("jarjish").password(passwordEncoder.encode("jarjish123")).roles("ROLE_ADMIN").build();
		UserDetails user2 = User.withUsername("shweta").password(passwordEncoder.encode("shwatass")).roles("ROLE_USER").build();
		return new InMemoryUserDetailsManager(user1, user2);
	}
}