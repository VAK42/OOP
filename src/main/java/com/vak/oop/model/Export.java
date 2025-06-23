package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Export {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID epId;
  @ManyToOne(optional = false)
  @JoinColumn(name = "pdId", nullable = false)
  private Product product;
  @Column(nullable = false)
  private BigDecimal pdPrice;
  @Column(nullable = false)
  private int pdQuantity;
  @Column(nullable = false)
  private BigDecimal pdTotalPrice;
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;
  @Column(nullable = false)
  private Timestamp date = new Timestamp(System.currentTimeMillis());

  @PrePersist
  @PreUpdate
  public void calculateTotalPrice() {
    if (pdPrice != null && pdQuantity > 0) {
      pdTotalPrice = pdPrice.multiply(BigDecimal.valueOf(pdQuantity));
    }
  }
}