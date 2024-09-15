package com.g12.tpo.server.service.interfaces;

import java.util.List;

import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.exceptions.ProductNotFoundException;

public interface ProductService {
    Product createProduct(Product product);

    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product productDetails) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;

    Product updateProductImageUrl(Long productId, String imageUrl);
}
