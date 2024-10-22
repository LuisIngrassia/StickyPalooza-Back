package com.g12.tpo.server.service.interfaces;

import java.util.List; 

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.exceptions.CategoryDuplicateException;

public interface CategoryService {
    public Page<Category> getCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryById(Long categoryId);

    public Category createCategory(String description) throws CategoryDuplicateException;

    public void deleteCategory(Long categoryId);
    
    public List<Category> getAllCategories();

    public Category updateCategory(Category category);
}
