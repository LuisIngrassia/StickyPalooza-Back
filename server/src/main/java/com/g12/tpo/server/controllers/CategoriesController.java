package com.g12.tpo.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.service.CategoryService;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @GetMapping
    public ArrayList<com.g12.tpo.server.entity.Category> getCategories() {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategories();
    }
    
    @GetMapping("/{categoryId}")
    public com.g12.tpo.server.entity.Category getCategoryById(@PathVariable int categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.getCategoryById(categoryId);
    }
    
    @PostMapping
    public String createCategory(@RequestBody String categoryId) {
        CategoryService categoryService = new CategoryService();
        return categoryService.createCategory(categoryId);
    }
    

}
