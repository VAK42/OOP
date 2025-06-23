package com.vak.oop.repository;

import com.vak.oop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  Page<User> findByRoleIgnoreCase(String role, Pageable pageable);

  Page<User> findByRoleIgnoreCaseAndUsernameContainingIgnoreCase(String role, String username, Pageable pageable);

  boolean existsByUsernameIgnoreCase(String username);
}