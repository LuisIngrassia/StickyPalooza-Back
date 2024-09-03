package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.entity.Role;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.dto.UserDTO;
import com.g12.tpo.server.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .cartId(user.getCart() != null ? user.getCart().getId() : null)
            .orderIds(user.getOrders().stream().map(order -> order.getId()).collect(Collectors.toList()))
            .billIds(user.getBills().stream().map(bill -> bill.getId()).collect(Collectors.toList()))
            .role(user.getRole().name())
            .build();
    }

    // Convert UserDTO to User
    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : null);
        return user;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        Page<User> usersPage = (page == null || size == null) ?
                userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE)) :
                userService.getAllUsers(PageRequest.of(page, size));
        
        Page<UserDTO> userDTOsPage = usersPage.map(this::convertToDTO);
        return ResponseEntity.ok(userDTOsPage);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        Optional<User> result = userService.getUserById(userId);
        return result.map(user -> ResponseEntity.ok(convertToDTO(user)))
                     .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @RequestBody UserDTO userRequest) {
        User user = convertToEntity(userRequest);
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
