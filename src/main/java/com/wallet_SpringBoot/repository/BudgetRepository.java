package com.wallet_SpringBoot.repository;

import com.wallet_SpringBoot.model.Budget;
import com.wallet_SpringBoot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
