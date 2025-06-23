package com.vak.oop.service;

import com.vak.oop.model.Report;
import com.vak.oop.repository.ReportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService {
  private final ReportRepository repo;

  public ReportService(ReportRepository repo) {
    this.repo = repo;
  }

  public Page<Report> findAll(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Page<Report> search(String keyword, Pageable pageable) {
    return repo.findByRpNameContainingIgnoreCase(keyword, pageable);
  }

  public void save(Report report) {
    repo.save(report);
  }

  public Optional<Report> findById(UUID id) {
    return repo.findById(id);
  }

  public void deleteById(UUID id) {
    repo.deleteById(id);
  }
}