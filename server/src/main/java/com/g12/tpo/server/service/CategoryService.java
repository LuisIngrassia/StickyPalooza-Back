package com.g12.tpo.server.service;

import java.util.ArrayList;

import com.g12.tpo.server.repository.CategoryRepository;

public class CategoryService {
    
    public ArrayList<com.g12.tpo.server.entity.Category> getCategories() {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategories();
    }

    public com.g12.tpo.server.entity.Category getCategoryById(int categoryId) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.getCategoryById(categoryId);
    }

    public String createCategory(String entity) {
        CategoryRepository categoryRepository = new CategoryRepository();
        return categoryRepository.createCategory(entity);
    }
}