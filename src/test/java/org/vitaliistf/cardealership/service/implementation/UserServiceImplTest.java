package org.vitaliistf.cardealership.service.implementation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.exception.ConflictException;
import org.vitaliistf.cardealership.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUserWithNewUser() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "1234567890", "password", "address");
        when(userRepository.findByEmailOrPhoneNumberExcludingCurrentUser(any(String.class), any(String.class), anyLong())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> userService.saveUser(user));
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testSaveUserWithExistingUser() {
        User user = new User(1L, "John", "Doe", "john.doe@example.com", "password", "1234567890", "address");
        User existingUser = new User(2L, "John", "Doe", "john.doe@example.com", "password", "0987654321", "address");
        when(userRepository.findByEmailOrPhoneNumberExcludingCurrentUser(any(String.class), any(String.class), anyLong())).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () -> userService.saveUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoadUserByUsername() {
        String email = "john.doe@example.com";
        User user = new User(1L, "John", "Doe", email, "1234567890", "password", "address");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }
}
