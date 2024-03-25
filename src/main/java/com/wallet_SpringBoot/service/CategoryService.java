package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.Category;
import com.wallet_SpringBoot.repository.CategoryRepository;
import com.wallet_SpringBoot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional
    public List<Category> findAllCategories() {
        return repository.findAll();
    }

    @Transactional
    public Category findCategoryById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public void autoFillCategories() {
        if (repository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    new Category("Groceries"),
                    new Category("Entertainment"),
                    new Category("Transportation"),
                    new Category("Housing"),
                    new Category("Health"),
                    new Category("Personal Expenses"),
                    new Category("Education"),
                    new Category("Bank Transfers"),
                    new Category("Travel"),
                    new Category("Electronics"),
                    new Category("Deposit"),
                    new Category("Goals")
            );
            repository.saveAll(categories);
        }
    }
}
