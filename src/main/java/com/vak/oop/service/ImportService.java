package com.vak.oop.service;

import com.vak.oop.model.Import;
import com.vak.oop.repository.ImportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ImportService {
  private final ImportRepository repo;

  public ImportService(ImportRepository repo) {
    this.repo = repo;
  }

  public Page<Import> findAll(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Import> search(String keyword, Pageable pageable) {
    return repo.findByProduct_PdNameContainingIgnoreCase(keyword, pageable);
  }

  public void save(Import imp) {
    repo.save(imp);
  }

  public Optional<Import> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }
}