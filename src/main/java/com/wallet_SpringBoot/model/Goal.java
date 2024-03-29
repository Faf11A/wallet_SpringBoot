package com.wallet_SpringBoot.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String goalName;

    private BigDecimal currentAmount;

    private BigDecimal targetAmount;

    private LocalDate targetDate;

    public Goal(User user,
                String goalName,
                BigDecimal currentAmount,
                BigDecimal targetAmount,
                LocalDate targetDate){
        this.user = user;
        this.goalName = goalName;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
        this.targetDate = targetDate;
    }
}
