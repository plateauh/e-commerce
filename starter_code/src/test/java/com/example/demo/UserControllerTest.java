package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Najed Alseghair at 1/30/2024
 */
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    /*
        Create User
     */

    @Test
    public void createUserHappyPath() {
        when(encoder.encode("testtest"))
                .thenReturn("hashed");

        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("testtest");
        request.setConfirmPassword("testtest");

        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashed", user.getPassword());
    }

    @Test
    public void createUserSadPathIncomplexPassword() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("test");
        request.setConfirmPassword("test");

        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createUserSadPathPasswordNotEqualsConfirmPassword() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("test");
        request.setConfirmPassword("test1");

        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /*
        find User By ID
     */

    @Test
    public void findUserByIdHappyPath() {
        when(userRepository.findById(0L))
                .thenReturn(Optional.of(TestUtils.getMockUser(0L, "test")));

        ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0L, responseBody.getId());
        assertEquals("test", responseBody.getUsername());
    }

    @Test
    public void findUserByIdSadPathWrongId() {
        when(userRepository.findById(0L))
                .thenReturn(Optional.of(TestUtils.getMockUser(0L, "test")));

        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
        find User By username
     */

    @Test
    public void findUserByUsernameHappyPath() {
        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test"));

        ResponseEntity<User> response = userController.findByUserName("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0L, responseBody.getId());
        assertEquals("test", responseBody.getUsername());
    }

    @Test
    public void findUserByUsernameSadPathWrongUsername() {
        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test"));

        ResponseEntity<User> response = userController.findByUserName("test1");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    
}
