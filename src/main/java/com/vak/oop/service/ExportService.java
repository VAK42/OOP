package com.vak.oop.service;

import com.vak.oop.model.Export;
import com.vak.oop.repository.ExportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExportService {
  private final ExportRepository repo;

  public ExportService(ExportRepository repo) {
    this.repo = repo;
  }

  public Page<Export> findAll(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Export> search(String keyword, Pageable pageable) {
    return repo.findByProduct_PdNameContainingIgnoreCase(keyword, pageable);
  }

  public void save(Export exp) {
    repo.save(exp);
  }

  public Optional<Export> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }
}