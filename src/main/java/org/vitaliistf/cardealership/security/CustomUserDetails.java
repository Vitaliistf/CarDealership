package org.vitaliistf.cardealership.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vitaliistf.cardealership.data.User;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation representing user details for authentication.
 */
public record CustomUserDetails(User user) implements UserDetails {

    /**
     * Returns an empty collection as there are no granted authorities for this user.
     *
     * @return an empty collection
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    /**
     * Retrieves the username of the user, which is their email.
     *
     * @return the email of the user
     */
    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    /**
     * Indicates whether the user's account has not expired.
     *
     * @return true, as the account never expires
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is not locked.
     *
     * @return true, as the account is never locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are not expired.
     *
     * @return true, as the credentials never expire
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true, as the user is always enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

