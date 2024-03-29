package com.wallet_SpringBoot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private BigDecimal amount;

    private LocalDate date;

    private String description;

    public Transaction(){}

    public Transaction(User user, Category category, BigDecimal amount, LocalDate date, String description){
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}

