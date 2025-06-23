package com.vak.oop.repository;

import com.vak.oop.model.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImportRepository extends JpaRepository<Import, UUID> {
  Page<Import> findByProduct_PdNameContainingIgnoreCase(String keyword, Pageable pageable);
}