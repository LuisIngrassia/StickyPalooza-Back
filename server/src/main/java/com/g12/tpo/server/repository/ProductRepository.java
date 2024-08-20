package com.g12.tpo.server.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import com.g12.tpo.server.entity.Category;
import com.g12.tpo.server.entity.Product;

public class ProductRepository {
    public ArrayList<Product> products = new ArrayList<Product>(
        Arrays.asList(
            Product.builder()
                .id(1L)
                .name("Producto 1")
                .description("Descripción del Producto 1")
                .price(new BigDecimal("10.00"))
                .category(Category.builder().description("Series").id(1).build())
                .stockQuantity(100)
                .build(),
            Product.builder()
                .id(2L)
                .name("Producto 2")
                .description("Descripción del Producto 2")
                .price(new BigDecimal("20.00"))
                .category(Category.builder().description("Peliculas").id(2).build())
                .stockQuantity(50)
                .build()
        )
    );

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public Product getProductById(Long productId) {
        return this.products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElse(null);
    }

    public String createProduct(Product product) {
        this.products.add(product);
        return "Product created successfully!";
    }
}
