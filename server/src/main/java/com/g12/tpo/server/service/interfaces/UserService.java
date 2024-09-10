package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(PageRequest pageRequest);

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User updatedUser);
}
