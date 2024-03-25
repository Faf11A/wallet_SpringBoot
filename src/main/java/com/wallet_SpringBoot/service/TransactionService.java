package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.Category;
import com.wallet_SpringBoot.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wallet_SpringBoot.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Transaction transaction) {
        entityManager.persist(transaction);
    }

    @Transactional
    public Optional<Transaction> getLastTransaction(Long userId) {
        String jpql = "SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.id DESC";
        TypedQuery<Transaction> query = entityManager.createQuery(jpql, Transaction.class)
                .setParameter("userId", userId)
                .setMaxResults(1);

        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Transaction> getAllTransactionsByUser(Long userId) {
        String queryString = "SELECT t FROM Transaction t WHERE t.user.id = :userId";
        TypedQuery<Transaction> query = entityManager.createQuery(queryString, Transaction.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional
    public List<Transaction> sortTransactions(List<Transaction> transactions, String sortBy, String sortOrder) {
        Comparator<Transaction> comparator;

        switch (sortBy) {
            case "amount":
                comparator = Comparator.comparing(Transaction::getAmount);
                break;
            case "date":
                comparator = Comparator.comparing(Transaction::getDate);
                break;
            case "category":
                comparator = Comparator.comparing(t -> t.getCategory().getCategory_name());
                break;
            default:
                return transactions;
        }

        transactions.sort(comparator);

        if ("desc".equals(sortOrder)) {
            Collections.reverse(transactions);
        }

        return transactions;
    }

    @Transactional
    public List<Transaction> filterByCategory(List<Transaction> transactions, Category selectedCategory) {
        return transactions.stream()
                .filter(transaction -> {
                    if (selectedCategory == null) {
                        return true;
                    }

                    Category transactionCategory = transaction.getCategory();
                    return transactionCategory != null && transactionCategory.getId().equals(selectedCategory.getId());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFirstTransaction(Category category, User user) {
        Transaction transaction = new Transaction();

        transaction.setAmount(BigDecimal.valueOf(0));
        transaction.setDate(LocalDate.of(1996, 1, 21));
        transaction.setDescription("Java was shown on this day");
        transaction.setCategory(category);
        transaction.setUser(user);

        entityManager.persist(transaction);
    }

    @Transactional
    public long getTransactionCountByCategory(Long categoryId) {
        String jpql = "SELECT COUNT(t) FROM Transaction t WHERE t.category.id = :categoryId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("categoryId", categoryId);
        return query.getSingleResult();
    }

    @Transactional
    public Double getTransactionSumByCategory(Long categoryId) {
        String jpql = "SELECT SUM(t.amount) FROM Transaction t WHERE t.category.id = :categoryId";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        query.setParameter("categoryId", categoryId);
        BigDecimal result = query.getSingleResult();
        return (result != null) ? result.doubleValue() : 0.0;
    }

    @Transactional
    public void deleteTransactionsByUserId(Long userId) {
        entityManager.createQuery("DELETE FROM Transaction t WHERE t.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
