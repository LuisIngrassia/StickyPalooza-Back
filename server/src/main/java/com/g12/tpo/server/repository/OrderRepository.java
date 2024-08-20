package com.g12.tpo.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.g12.tpo.server.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
}
