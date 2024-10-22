package com.g12.tpo.server.service.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.g12.tpo.server.service.interfaces.CategoryService;
import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.exceptions.CategoryDuplicateException;
import com.g12.tpo.server.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category createCategory(String description) throws CategoryDuplicateException {
        List<Category> categories = categoryRepository.findByDescription(description);
        if (categories.isEmpty()) {
            var category = Category.builder().description(description).build();
            return categoryRepository.save(category);
        }
        throw new CategoryDuplicateException();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
