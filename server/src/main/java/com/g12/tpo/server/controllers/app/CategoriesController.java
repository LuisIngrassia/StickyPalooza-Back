package com.g12.tpo.server.controllers.app;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
import com.g12.tpo.server.service.CategoryService;

@RestController
@RequestMapping("categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getCategories(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Page<Category> categoryPage;
        if (page == null || size == null) {
            categoryPage = categoryService.getCategories(PageRequest.of(0, Integer.MAX_VALUE));
        } else {
            categoryPage = categoryService.getCategories(PageRequest.of(page, size));
        }
        Page<CategoryDTO> categoryDTOPage = categoryPage.map(this::convertToDTO);
        return ResponseEntity.ok(categoryDTOPage);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> result = categoryService.getCategoryById(categoryId);
        if (result.isPresent()) {
            return ResponseEntity.ok(convertToDTO(result.get()));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO)
            throws CategoryDuplicateException {
        Category result = categoryService.createCategory(categoryDTO.getDescription());
        return ResponseEntity.created(URI.create("/categories/" + result.getId()))
                             .body(convertToDTO(result));
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
