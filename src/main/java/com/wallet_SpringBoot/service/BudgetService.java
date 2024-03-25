package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.Budget;
import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.repository.BudgetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository repository;
    @PersistenceContext
    private EntityManager entityManager;
    public void save(Budget budget) {
        repository.save(budget);
    }

    @Transactional
    public Budget update(Budget budget) {
        Budget budgetFromDb = repository.findById(budget.getId()).orElse(null);

        if (budgetFromDb != null) {
            budgetFromDb.setAmount(budget.getAmount());

            budgetFromDb = repository.save(budgetFromDb);
            return budgetFromDb;
        } else {
            return null;
        }
    }

    @Transactional
    public Optional<Budget> getBudgetByUserId(Long userId) {
        String jpql = "SELECT b FROM Budget b WHERE b.user.id = :userId";
        try {
            Budget budget = entityManager.createQuery(jpql, Budget.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(budget);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void updateBalance(Long userId, BigDecimal amount) {
        String jpql = "UPDATE Budget b SET b.amount = b.amount + :amount WHERE b.user.id = :userId";
        entityManager.createQuery(jpql)
                .setParameter("amount", amount)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Transactional
    public void createFirstBudgetForNewUser(User user) {
        Budget budget = new Budget();
        budget.setName("My wallet");
        budget.setAmount(BigDecimal.valueOf(0));
        budget.setUser(user);
        entityManager.persist(budget);
    }
    @Transactional
    public void deleteBudgetsByUserId(Long userId) {
        entityManager.createQuery("DELETE FROM Budget t WHERE t.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
