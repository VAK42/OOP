package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Category {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "categoryId", updatable = false, nullable = false)
  private UUID categoryId;
  @Column(nullable = false, unique = true, length = 50)
  private String name;
}