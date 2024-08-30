package com.g12.tpo.server.repository;

import java.util.Optional;

import com.g12.tpo.server.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String mail);
}
