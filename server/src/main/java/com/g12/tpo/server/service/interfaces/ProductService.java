package com.g12.tpo.server.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.exceptions.ProductNotFoundException;

public interface ProductService {
    Product createProduct(Product product, MultipartFile imageFile);

    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product productDetails, MultipartFile imageFile) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;

    Product updateProductImage(Long productId, String image);

    List<Product> getProductsByName(String name);
    
    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}
