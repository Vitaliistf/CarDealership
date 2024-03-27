package org.vitaliistf.cardealership.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.vitaliistf.cardealership.data.User;

/**
 * Implementation of the {@link UserService} interface for managing user-related operations.
 */
public interface UserService extends UserDetailsService {

    /**
     * Saves a user to the database.
     *
     * @param user The user to be saved
     */
    void saveUser(User user);

}
