package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.config.DateConverter;
import com.wallet_SpringBoot.model.*;
import com.wallet_SpringBoot.service.CategoryService;
import com.wallet_SpringBoot.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Component
public class DemoUser {
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    BudgetService budgetService;
    @Autowired
    GoalService goalService;
    @Autowired
    TransactionService transactionService;
    @Transactional
    public void CreateDemoUser(){
        if (!userService.isUserExistsByLogin("test")) {
            User testUser = new User("test", "test");
            LocalDate localDate = LocalDate.now();
            Date date = DateConverter.convertToLocalDateViaInstant(localDate);
            UserDetails testUserDetails = new UserDetails(testUser, "Name", "LastName", "example@gmail.com", date);
            Budget budget = new Budget(testUser, "test", BigDecimal.valueOf(3350));

            LocalDate goal1Date = LocalDate.of(2024, 12, 31);
            LocalDate goal2Date = LocalDate.of(2024, 7, 31);
            Goal goal1 = new Goal(testUser, "Car", BigDecimal.valueOf(6550), BigDecimal.valueOf(12000), goal1Date);
            Goal goal2 = new Goal(testUser, "Travel", BigDecimal.valueOf(1500), BigDecimal.valueOf(2200), goal2Date);

            LocalDate transactionDate1 = LocalDate.of(2024, 1, 31);
            LocalDate transactionDate2 = LocalDate.of(2024, 2, 1);
            LocalDate transactionDate3 = LocalDate.of(2024, 2, 3);
            LocalDate transactionDate4 = LocalDate.of(2024, 2, 6);
            Transaction transaction1 = new Transaction(testUser, categoryService.findCategoryById(1L), BigDecimal.valueOf(130), transactionDate1, "demo description");
            Transaction transaction2 = new Transaction(testUser, categoryService.findCategoryById(2L), BigDecimal.valueOf(50), transactionDate2, "demo description");
            Transaction transaction3 = new Transaction(testUser, categoryService.findCategoryById(3L), BigDecimal.valueOf(70), transactionDate2, "demo description");
            Transaction transaction4 = new Transaction(testUser, categoryService.findCategoryById(6L), BigDecimal.valueOf(100), transactionDate2, "demo description");
            Transaction transaction5 = new Transaction(testUser, categoryService.findCategoryById(8L), BigDecimal.valueOf(50), transactionDate3, "demo description");
            Transaction transaction6 = new Transaction(testUser, categoryService.findCategoryById(5L), BigDecimal.valueOf(30), transactionDate4, "demo description");

            userService.saveUser(testUser);
            userDetailsService.saveUserDetails(testUserDetails);
            budgetService.save(budget);
            goalService.saveGoal(goal1);
            goalService.saveGoal(goal2);
            transactionService.save(transaction1);
            transactionService.save(transaction2);
            transactionService.save(transaction3);
            transactionService.save(transaction4);
            transactionService.save(transaction5);
            transactionService.save(transaction6);
        }
    }
}
