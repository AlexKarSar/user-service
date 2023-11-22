package com.example.userservice.dto.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_table")
public class UserEntity {

    private String username;

    private String name;

    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private String refreshToken;
}