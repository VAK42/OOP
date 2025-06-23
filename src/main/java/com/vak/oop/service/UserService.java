package com.vak.oop.service;

import com.vak.oop.model.User;
import com.vak.oop.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
  private final UserRepository repo;

  public UserService(UserRepository repo) {
    this.repo = repo;
  }

  public Page<User> findByRole(String role, Pageable pageable) {
    return repo.findByRoleIgnoreCase(role, pageable);
  }

  public void save(User user) {
    repo.save(user);
  }

  public Optional<User> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  public Page<User> searchByUsernameAndRole(String role, String keyword, Pageable pageable) {
    return repo.findByRoleIgnoreCaseAndUsernameContainingIgnoreCase(role, keyword, pageable);
  }

  public boolean existsByUsername(String username) {
    return repo.existsByUsernameIgnoreCase(username);
  }
}