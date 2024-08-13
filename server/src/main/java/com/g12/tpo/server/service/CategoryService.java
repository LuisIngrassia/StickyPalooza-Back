package com.g12.tpo.server.service;

import java.util.ArrayList;

import com.g12.tpo.server.repository.CategoryRepository;
import com.g12.tpo.server.entity.Category;

public class CategoryService {
    
    public ArrayList<Category> getCategories() {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategories();
    }

    public Category getCategoryById(int categoryId) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategoryById(categoryId);
    }

    public String createCategory(String entity) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.createCategory(entity);
    }
}