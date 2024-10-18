package com.g12.tpo.server.controllers.app;

import com.g12.tpo.server.entity.Role;
import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.dto.UserDTO;
import com.g12.tpo.server.dto.ShowUserDTO;
import com.g12.tpo.server.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    // Convert User to UserDTO without accessing lazy-loaded collections
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .cartId(user.getCart() != null ? user.getCart().getId() : null)
            // Avoid accessing lazy-loaded collections here
            .role(user.getRole().name())
            .build();
    }

    private ShowUserDTO convertToShowUserDTO(User user) {
        return ShowUserDTO.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .password(user.getPassword())  
            .build();
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
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
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

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ShowUserDTO> getLoggedInUser(Authentication authentication) {
        String loggedInEmail = authentication.getName();  
        Optional<User> result = userService.getUserByEmail(loggedInEmail);

        if (result.isPresent()) {
            User user = result.get();
            ShowUserDTO userDTO = convertToShowUserDTO(user); 
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : null);
        return user;
    }
}
