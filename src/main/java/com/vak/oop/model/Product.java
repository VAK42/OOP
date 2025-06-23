package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Product {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "pdId", updatable = false, nullable = false)
  private UUID pdId;
  @Column(nullable = false, unique = true, length = 100)
  private String pdName;
  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal pdPrice;
  @Column(nullable = false)
  private String pdInfo;
  @Column(nullable = false)
  private int pdQuantity = 0;
  @ManyToOne
  @JoinColumn(name = "categoryId", nullable = false)
  private Category category;
}