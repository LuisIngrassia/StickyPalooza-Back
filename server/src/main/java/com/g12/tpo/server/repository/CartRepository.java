package com.g12.tpo.server.repository;

import com.g12.tpo.server.entity.Cart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
