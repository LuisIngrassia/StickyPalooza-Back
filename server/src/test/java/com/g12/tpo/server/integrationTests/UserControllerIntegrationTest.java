// package com.g12.tpo.server.integrationTests;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.g12.tpo.server.entity.Role;
// import com.g12.tpo.server.entity.User;
// import com.g12.tpo.server.entity.dto.UserDTO;
// import com.g12.tpo.server.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @SpringBootTest
// @ActiveProfiles("test")
// public class UserControllerIntegrationTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     private User testUser;

//     @BeforeEach
//     public void setUp() {
//         testUser = new User();
//         testUser.setEmail("testuser@example.com");
//         testUser.setName("Test User");
//         testUser.setFirstName("Test");
//         testUser.setLastName("User");
//         testUser.setRole(Role.USER);
//         userRepository.save(testUser);
//     }

//     @Test
//     public void testGetAllUsers() throws Exception {
//         ResultActions result = mockMvc.perform(get("/users")
//                 .param("page", "0")
//                 .param("size", "10")
//                 .header("Authorization", "Bearer someValidToken"));

//         result.andExpect(status().isOk())
//               .andExpect(jsonPath("$.content[0].email").value(testUser.getEmail()));
//     }

//     @Test
//     public void testGetUserById() throws Exception {
//         ResultActions result = mockMvc.perform(get("/users/{userId}", testUser.getId())
//                 .header("Authorization", "Bearer someValidToken"));

//         result.andExpect(status().isOk())
//               .andExpect(jsonPath("$.email").value(testUser.getEmail()));
//     }

//     @Test
//     public void testUpdateUser() throws Exception {
//         UserDTO userDTO = UserDTO.builder()
//                 .id(testUser.getId())
//                 .email("updated@example.com")
//                 .name("Updated Name")
//                 .firstName("Updated")
//                 .lastName("Name")
//                 .role("USER")
//                 .build();

//         ResultActions result = mockMvc.perform(put("/users/{userId}", testUser.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(userDTO))
//                 .header("Authorization", "Bearer someValidToken"));

//         result.andExpect(status().isOk())
//               .andExpect(jsonPath("$.email").value("updated@example.com"));
//     }

//     @Test
//     public void testDeleteUser() throws Exception {
//         ResultActions result = mockMvc.perform(delete("/users/{userId}", testUser.getId())
//                 .header("Authorization", "Bearer someValidToken"));

//         result.andExpect(status().isNoContent());
//         assertEquals(0, userRepository.count());
//     }
// }
