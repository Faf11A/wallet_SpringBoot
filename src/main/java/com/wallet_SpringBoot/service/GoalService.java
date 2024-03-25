package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.Goal;
import com.wallet_SpringBoot.model.User;
import com.wallet_SpringBoot.repository.BudgetRepository;
import com.wallet_SpringBoot.repository.GoalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {
    @Autowired
    private GoalRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public void saveGoal(Goal goal) {
        repository.save(goal);
    }

    public List<Goal> findAllGoals() {
        return repository.findAll();
    }

    public List<Goal> findGoalsByUser(User user) {
        return repository.findByUserId(user.getId());
    }

    public Goal findGoalById(Long goalId) {
        return repository.findById(goalId).orElse(null);
    }

    public List<Goal> getAllGoalsForUser(long userId) {
        return repository.findAllByUserId(userId);
    }

    public List<Goal> findCurrentGoals(Long userId) {
        String hql = "FROM Goal g WHERE g.user.id = :userId AND ((g.currentAmount / g.targetAmount) * 100) < 100";
        return entityManager.createQuery(hql, Goal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Goal> findCompletedGoals(Long userId) {
        String hql = "FROM Goal g WHERE g.user.id = :userId AND ((g.currentAmount / g.targetAmount) * 100) >= 100";
        return entityManager.createQuery(hql, Goal.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    public void deleteGoal(Long goalId) {
        repository.deleteById(goalId);
    }

    public List<Goal> findExpiredGoals(Long userId) {
        LocalDate currentDate = LocalDate.now();

        List<Goal> expiredGoals = new ArrayList<>();
        List<Goal> allGoals = getAllGoalsForUser(userId);


        for (Goal goal : allGoals) {
            LocalDate targetDate = goal.getTargetDate();

            BigDecimal amountCur = goal.getCurrentAmount();
            BigDecimal amountTar = goal.getTargetAmount();
            int comparison = amountCur.compareTo(amountTar);

            if (targetDate.isBefore(currentDate) && comparison < 0) {
                expiredGoals.add(goal);
            }
        }
        return expiredGoals;
    }

    public void deleteGoalsByUserId(Long userId) {
        entityManager.createQuery("DELETE FROM Goal t WHERE t.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
