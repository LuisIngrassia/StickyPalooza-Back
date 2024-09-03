package com.g12.tpo.server.service.interfaces;

import com.g12.tpo.server.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);

    User updateUser(Long id, User updatedUser);
}
