package org.vitaliistf.cardealership.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.exception.ConflictException;
import org.vitaliistf.cardealership.repository.UserRepository;
import org.vitaliistf.cardealership.security.CustomUserDetails;
import org.vitaliistf.cardealership.service.UserService;

import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface for managing user-related operations.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Saves the user to the repository after encoding the password.
     * Throws a ConflictException if a user with the same email or phone number already exists.
     *
     * @param user User object to be saved
     */
    @Override
    public void saveUser(User user) {
        long id = user.getId() == null ? 0 : user.getId();
        Optional<User> existingUserByEmailOrPhoneNumber = userRepository.findByEmailOrPhoneNumberExcludingCurrentUser(
                user.getEmail(), user.getPhoneNumber(), id
        );

        if (existingUserByEmailOrPhoneNumber.isPresent()) {
            throw new ConflictException("User with the same email or phone number already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Loads the user details by the provided email.
     * Throws a UsernameNotFoundException if the user is not found.
     *
     * @param email Email of the user
     * @return UserDetails object containing user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new CustomUserDetails(user.get());
    }

}
