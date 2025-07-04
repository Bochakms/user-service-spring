package io.github.Bochakms.service;

import io.github.Bochakms.dto.UserRequest;
import io.github.Bochakms.dto.UserResponse;
import io.github.Bochakms.entity.User;
import io.github.Bochakms.exeption.UserNotFoundException;
import io.github.Bochakms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        UserRequest userRequest = new UserRequest("Garry", "Potter", "garry@hogwards.com", "patronus123");
        User user = new User();
        user.setId(1L);
        user.setFirstName("Garry");
        user.setLastName("Potter");
        user.setEmail("garry@hogwards.com");
        user.setPassword("patronus123");

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.createUser(userRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Garry", response.getFirstName());
        assertEquals("Potter", response.getLastName());
        assertEquals("garry@hogwards.com", response.getEmail());
    }

    @Test
    void shouldGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Geralt");
        user.setLastName("FromRivia");
        user.setEmail("geralt@kaermorhen.com");
        user.setPassword("witcher123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("Geralt", response.getFirstName());
        assertEquals("FromRivia", response.getLastName());
        assertEquals("geralt@kaermorhen.com", response.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void shouldGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Frodo");
        user.setLastName("Baggins");
        user.setEmail("baggins@shire.com");
        user.setPassword("precious123");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Frodo", responses.get(0).getFirstName());
    }

    @Test
    void shouldUpdateUser() {
        Long userId = 1L;
        UserRequest updateRequest = new UserRequest("New", "Name", "new@test.com", "newpassword123");
        
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setFirstName("Old");
        existingUser.setLastName("Name");
        existingUser.setEmail("old@test.com");
        existingUser.setPassword("oldpassword123");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(userId, updateRequest);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("New", response.getFirstName());
        assertEquals("Name", response.getLastName());
        assertEquals("new@test.com", response.getEmail());
    }

    @Test
    void shouldDeleteUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }
}