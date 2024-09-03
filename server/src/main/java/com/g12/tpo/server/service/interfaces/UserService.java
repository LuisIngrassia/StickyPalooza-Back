package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    Page<User> getAllUsers(PageRequest pageRequest);

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User updatedUser);
}
