package com.g12.tpo.server.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale.Category;


public class CategoryRepository {
    public ArrayList<Category> categories = new ArrayList<Category>(
        Arrays.asList(Category.builder().description("Series").id(1).build(),
        Category.builder().description("Peliculas").id(2).build(),
        Category.builder().description("Musica").id(3).build())
        );

        public ArrayList<Category> getCategories() {
            return this.categories;
        }

        public Category getCategoryById(int categoryId) {
            return null;
        }

        public String createCategory(String entity) {
            return null;
        }
    
}
