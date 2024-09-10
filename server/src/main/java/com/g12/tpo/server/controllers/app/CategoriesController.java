package com.g12.tpo.server.controllers.app;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g12.tpo.server.dto.CategoryDTO;
import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.exceptions.CategoryDuplicateException;
import com.g12.tpo.server.service.interfaces.CategoryService;

@RestController
@RequestMapping("categories")
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
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Page<Category> categoriesPage = (page == null || size == null) ?
                categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE)) :
                categoryService.getCategories(PageRequest.of(page, size));
        
        Page<CategoryDTO> categoryDTOsPage = categoriesPage.map(this::convertToDTO);
        return ResponseEntity.ok(categoryDTOsPage);
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
}
