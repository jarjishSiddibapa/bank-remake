package com.aurionpro.bankremake.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aurionpro.bankremake.entity.User;
import com.aurionpro.bankremake.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = ?1")
    long countByRole(UserRole role);
}