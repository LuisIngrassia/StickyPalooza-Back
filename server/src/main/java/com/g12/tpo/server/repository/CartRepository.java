package com.g12.tpo.server.repository;

import com.g12.tpo.server.entity.Cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartProducts")
    List<Cart> findAllWithCartProducts();

    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    Cart findByCartId(@Param("cartId") Long cartId);
}