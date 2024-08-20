package com.g12.tpo.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.g12.tpo.server.entity.Category;

@Repository
public interface OrderRepository extends JpaRepository<Category, Long>{
    
    @Query(value =  "SELECT c FROM Category c WHERE c.description = ?1")
    List<Category> findByDescription(String description);
    
}
