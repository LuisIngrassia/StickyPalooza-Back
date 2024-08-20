package com.g12.tpo.server.repository;

import com.g12.tpo.server.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}