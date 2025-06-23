package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Report {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID reportId;
  @ManyToOne(optional = false)
  @JoinColumn(name = "userId", nullable = false)
  private User user;
  @Column(nullable = false, length = 100)
  private String rpName;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String rpInfo;
}