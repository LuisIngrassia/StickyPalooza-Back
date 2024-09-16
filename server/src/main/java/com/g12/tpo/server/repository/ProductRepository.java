package com.g12.tpo.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g12.tpo.server.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
}
