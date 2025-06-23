package com.vak.oop.repository;

import com.vak.oop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Page<Product> findByPdNameContainingIgnoreCase(String keyword, Pageable pageable);

  boolean existsByPdNameIgnoreCase(String pdName);
}