package com.g12.tpo.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g12.tpo.server.entity.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    List<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);
}
