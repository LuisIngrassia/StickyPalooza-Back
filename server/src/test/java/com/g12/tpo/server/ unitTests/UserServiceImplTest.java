import com.g12.tpo.server.entity.User;
import com.g12.tpo.server.repository.UserRepository;
import com.g12.tpo.server.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        List<User> users = List.of(user);
        Page<User> page = new PageImpl<>(users);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Mockito.when(userRepository.findAll(pageRequest)).thenReturn(page);

        Page<User> result = userService.getAllUsers(pageRequest);

        assertEquals(1, result.getTotalElements());
        assertEquals(user.getEmail(), result.getContent().get(0).getEmail());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertEquals(userId, result.get().getId());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        Mockito.when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        Mockito.verify(userRepository).deleteById(userId);
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        User updatedUser = new User();
        updatedUser.setEmail("newemail@example.com");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertEquals("newemail@example.com", result.getEmail());
    }
}
