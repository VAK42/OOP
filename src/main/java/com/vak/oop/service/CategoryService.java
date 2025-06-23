package com.vak.oop.service;

import com.vak.oop.model.Category;
import com.vak.oop.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
  private final CategoryRepository repo;

  public CategoryService(CategoryRepository repo) {
    this.repo = repo;
  }

  public Page<Category> findAll(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Category> search(String keyword, Pageable pageable) {
    return repo.findByNameContainingIgnoreCase(keyword, pageable);
  }

  public void save(Category category) {
    repo.save(category);
  }

  public Optional<Category> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  public boolean existsByName(String name) {
    return repo.existsByNameIgnoreCase(name);
  }
}