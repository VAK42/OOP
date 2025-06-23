package com.vak.oop.repository;

import com.vak.oop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
  Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

  boolean existsByNameIgnoreCase(String name);
}