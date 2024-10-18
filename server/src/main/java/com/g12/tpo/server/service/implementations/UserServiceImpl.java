package com.g12.tpo.server.service.implementations;

import com.g12.tpo.server.entity.Role;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder; 

    @Override
    public Page<User> getAllUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) { 
        return userRepository.findByEmail(email); 
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
public User updateUser(Long id, User updatedUser) {
    return userRepository.findById(id).map(user -> {
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
            user.setPassword(hashedPassword);  
        }
        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        if (user.getRole() == Role.ADMIN) {
            user.setCart(null);
        }

        return userRepository.save(user);
    }).orElseThrow(() -> new RuntimeException("User not found"));
}

    
}
