package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Import {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID ipId;
  @ManyToOne(optional = false)
  @JoinColumn(name = "pdId", nullable = false)
  private Product product;
  @Column(nullable = false)
  private BigDecimal pdPrice;
  @Column(nullable = false)
  private int pdQuantity;
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;
  @Column(nullable = false)
  private Timestamp date = new Timestamp(System.currentTimeMillis());
}