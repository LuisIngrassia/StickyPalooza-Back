package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.exceptions.ProductNotFoundException;

import java.util.List;

public interface ProductService {
    Product createProduct(Product product);

    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product productDetails) throws ProductNotFoundException;

    void deleteProduct(Long id) throws ProductNotFoundException;
}
