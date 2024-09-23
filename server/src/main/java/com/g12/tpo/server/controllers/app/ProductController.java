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
import com.g12.tpo.server.service.interfaces.ProductService;
import com.g12.tpo.server.service.implementations.ImagenesProdService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImagenesProdService fileUploadService;

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stockQuantity(product.getStockQuantity())
            .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
            .image(product.getImage())
            .build();
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setImage(productDTO.getImage());

        Category category = categoryService.getCategoryById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        product.setCategory(category);

        return product;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(convertToDTO(product));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(convertToDTO(createdProduct));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product productDetails = convertToEntity(productDTO);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(convertToDTO(updatedProduct));
    }    

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint for handling image uploads and updating the image URL
    @PostMapping("/{productId}/image")
    public ResponseEntity<ProductDTO> uploadProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile imageFile) {
        try {
            
            String image = fileUploadService.uploadImage(imageFile);

            Product updatedProduct = productService.updateProductImage(productId, image);

            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<ProductDTO>> getProductsByName(@RequestParam String name) {
        List<Product> products = productService.getProductsByName(name);
        List<ProductDTO> productDTOs = products.stream()
                                               .map(this::convertToDTO)
                                               .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/byPrice")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<ProductDTO>> getProductsByPriceRange(
        @RequestParam BigDecimal minPrice, 
        @RequestParam BigDecimal maxPrice) {
            List<Product> products = productService.getProductsByPriceRange(minPrice, maxPrice);
            List<ProductDTO> productDTOs = products.stream()
                                           .map(this::convertToDTO)
                                           .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
    }
}
