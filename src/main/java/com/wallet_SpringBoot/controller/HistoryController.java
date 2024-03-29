package com.wallet_SpringBoot.controller;

import com.wallet_SpringBoot.model.Category;
import com.wallet_SpringBoot.model.Transaction;
import com.wallet_SpringBoot.service.CategoryService;
import com.wallet_SpringBoot.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    //GET for history and filtration
    @GetMapping("/history")
    public String getTransactionHistory(Model model,
                                        HttpSession session,
                                        @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                        @RequestParam(name = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
                                        @RequestParam(name = "category", required = false, defaultValue = "0") Long categoryId) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        List<Transaction> transactions = transactionService.getAllTransactionsByUser(userId);

        //filtration by category
        Category selectedCategory = (categoryId != 0) ? categoryService.findCategoryById(categoryId) : null;
        transactions = transactionService.filterByCategory(transactions, selectedCategory);

        //filtration by and sort order
        transactions = transactionService.sortTransactions(transactions, sortBy, sortOrder);

        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("selectedCategory", categoryId);

        return "history";
    }

    //POST for history and filtration
    @PostMapping("/history")
    public String postTransactionHistory(Model model,
                                         HttpSession session,
                                         @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                         @RequestParam(name = "sortOrder", required = false, defaultValue = "desc") String sortOrder,
                                         @RequestParam(name = "category", required = false, defaultValue = "0") Long categoryId) {

        Long userId = (Long) session.getAttribute("userId");
        List<Transaction> transactions = transactionService.getAllTransactionsByUser(userId);

        Category selectedCategory = (categoryId != 0) ? categoryService.findCategoryById(categoryId) : null;
        transactions = transactionService.filterByCategory(transactions, selectedCategory);

        transactions = transactionService.sortTransactions(transactions, sortBy, sortOrder);

        List<Category> categories = categoryService.findAllCategories();

        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("selectedCategory", categoryId);

        return "history";
    }
}
