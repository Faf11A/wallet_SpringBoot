package com.wallet_SpringBoot.repository;

import com.wallet_SpringBoot.model.Budget;
import com.wallet_SpringBoot.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);
    List<Goal> findAllByUserId(Long userId);
}
