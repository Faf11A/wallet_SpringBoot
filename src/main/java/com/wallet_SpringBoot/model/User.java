package com.wallet_SpringBoot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(min = 4, max = 20)
    @Column(unique = true)
    private String login;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}