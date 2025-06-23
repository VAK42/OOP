package com.vak.oop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
  @Id
  @GeneratedValue(generator = "UUID")
  @Column(name = "userId", updatable = false, nullable = false)
  private UUID userId;
  @Column(nullable = false, unique = true, length = 50)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false, unique = true, length = 100)
  private String email;
  @Column(nullable = false, length = 20)
  private String role;
}