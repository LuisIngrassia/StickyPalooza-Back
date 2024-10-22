package com.g12.tpo.server.controllers.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.g12.tpo.server.dto.ProductDTO;
import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.service.interfaces.CategoryService;
import com.g12.tpo.server.service.interfaces.FileUploadService;
import com.g12.tpo.server.service.interfaces.ProductService;

import java.io.IOException;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileUploadService fileUploadService;

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stockQuantity(product.getStockQuantity())
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .categoryDescription(product.getCategory() != null ? product.getCategory().getDescription() : null)
            .image(product.getImage())
            .build();
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(convertToDTO(product));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stockQuantity") Integer stockQuantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException { // Add throws IOException
    
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
    
        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
    
        if (image != null && !image.isEmpty()) {
            String imagePath = fileUploadService.uploadImage(image); 
            product.setImage(imagePath);
        }
    
        Product createdProduct = productService.createProduct(product, product.getImage());
    
        return ResponseEntity.status(201).body(convertToDTO(createdProduct));
    }
    
    
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) BigDecimal price,
            @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Product productDetails = productService.getProductById(id);
    
        if (name != null) productDetails.setName(name);
        if (description != null) productDetails.setDescription(description);
        if (price != null) productDetails.setPrice(price);
        if (stockQuantity != null) productDetails.setStockQuantity(stockQuantity);
        
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            productDetails.setCategory(category);
        }
    
        if (image != null && !image.isEmpty()) {
            try {
                String imagePath = fileUploadService.uploadImage(image); 
                productDetails.setImage(imagePath);
            } catch (IOException e) {
                return ResponseEntity.status(500).body(null); 
            }
        }
    
        Product updatedProduct = productService.updateProduct(id, productDetails, productDetails.getImage());
    
        return ResponseEntity.ok(convertToDTO(updatedProduct));
    }
    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByName(@RequestParam String name) {
        List<Product> products = productService.getProductsByName(name);
        List<ProductDTO> productDTOs = products.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/byPrice")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        List<ProductDTO> productDTOs = products.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }
}
