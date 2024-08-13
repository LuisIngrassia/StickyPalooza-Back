package com.g12.tpo.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @GetMapping
    public String getCategories() {
        return new String();
    }
    
    @GetMapping("/{categoryId}")
    public String getCategoryById(@PathVariable String categoryId) {
        return new String();
    }
    
    @PostMapping
    public String createCategory(@RequestBody String entity) {
        return entity;
    }
    

}
