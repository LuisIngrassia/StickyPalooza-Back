package com.g12.tpo.server.controllers.app;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.dto.CategoryDTO;
import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.exceptions.CategoryDuplicateException;
import com.g12.tpo.server.service.interfaces.CategoryService;

@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
            .id(category.getId())
            .description(category.getDescription())
            .build();
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setDescription(dto.getDescription());
        return category;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<Category> categoriesList = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categoriesList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }


    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        return result.map(category -> ResponseEntity.ok(convertToDTO(category)))
                     .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryRequest)
            throws CategoryDuplicateException {
        Category category = convertToEntity(categoryRequest);
        Category result = categoryService.createCategory(category.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId()))
                             .body(convertToDTO(result));
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{categoryId}") 
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryRequest) {
        Category categoryToUpdate = convertToEntity(categoryRequest);
        categoryToUpdate.setId(categoryId);
        Category updatedCategory = categoryService.updateCategory(categoryToUpdate);
        return ResponseEntity.ok(convertToDTO(updatedCategory));
    }
}
