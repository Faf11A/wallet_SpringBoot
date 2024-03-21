package com.wallet_SpringBoot.service;

import com.wallet_SpringBoot.model.Category;
import com.wallet_SpringBoot.repository.CategoryRepository;
import com.wallet_SpringBoot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
