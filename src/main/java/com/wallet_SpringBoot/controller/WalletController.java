package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.*;
import com.wallet_SpringBoot.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Controller
public class WalletController {
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BudgetService budgetService;
    @Autowired
    TransactionService transactionService;

    @GetMapping("/wallet")
    public String showWallet(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        Long userId = (Long) session.getAttribute("userId");

        Optional<Budget> budgetOptional = budgetService.getBudgetByUserId(userId);
        Optional<Transaction> transactionOptional = transactionService.getLastTransaction(userId);

        if (budgetOptional.isPresent() && transactionOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            Transaction transaction = transactionOptional.get();
            Category category = transaction.getCategory();

            model.addAttribute("budget", budget);
            model.addAttribute("name_wall", budget.getName());
            model.addAttribute("balance", budget.getAmount());
            model.addAttribute("username", username);
            model.addAttribute("lastTrAmount", transaction.getAmount());
            model.addAttribute("lastTrDate", transaction.getDate());
            model.addAttribute("lastTrCategory", (category != null) ? category.getCategory_name() : "No Category");

            return "wallet";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/wallet")
    public String addFunds(HttpSession session, BigDecimal amount) {
        Long userId = (Long) session.getAttribute("userId");

        budgetService.updateBalance(userId, amount);

        Transaction newTransaction = new Transaction();

        newTransaction.setUser(userService.findUserById(userId));
        newTransaction.setAmount(amount);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(formatter);
        newTransaction.setDate(LocalDate.parse(formattedDate, formatter));

        newTransaction.setDescription("Deposit by main page");
        newTransaction.setCategory(categoryService.findCategoryById(11L));

        transactionService.save(newTransaction);
        return "redirect:/wallet";
    }
}
