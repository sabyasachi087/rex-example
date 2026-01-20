package com.sroyc.rex.example.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("USERS")
public class User {

    @Id
    private Long userId;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private Boolean isActive;
}
