package org.vitaliistf.cardealership.util;

import org.springframework.security.core.Authentication;
import org.vitaliistf.cardealership.data.User;
import org.vitaliistf.cardealership.security.CustomUserDetails;

/**
 * Utility class for extracting User information from Authentication.
 */
public class UserExtractor {

    /**
     * Retrieves the User object from the authentication details.
     *
     * @param authentication the authentication object
     * @return the User object extracted from the authentication
     * @throws ClassCastException if the authentication principal is not an instance of CustomUserDetails
     */
    public static User getUser(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).user();
    }

}
