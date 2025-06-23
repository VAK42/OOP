package com.vak.oop.service;

import com.vak.oop.model.Product;
import com.vak.oop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  public Page<Product> findAll(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Product> search(String keyword, Pageable pageable) {
    return repo.findByPdNameContainingIgnoreCase(keyword, pageable);
  }

  public void save(Product product) {
    repo.save(product);
  }

  public Optional<Product> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }

  public boolean existsByPdName(String name) {
    return repo.existsByPdNameIgnoreCase(name);
  }
}