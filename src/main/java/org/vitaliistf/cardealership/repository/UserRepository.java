package org.vitaliistf.cardealership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vitaliistf.cardealership.data.User;

import java.util.Optional;

/**
 * Repository interface for accessing User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user
     * @return an optional user matching the email, if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user by email or phone number, excluding the current user.
     *
     * @param email         the email of the user
     * @param phoneNumber   the phone number of the user
     * @param currentUserId the id of the current user to be excluded from the search
     * @return an optional user matching the email or phone number, excluding the current user, if found
     */
    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.phoneNumber = :phoneNumber) AND u.id <> :currentUserId")
    Optional<User> findByEmailOrPhoneNumberExcludingCurrentUser(@Param("email") String email,
                                                                @Param("phoneNumber") String phoneNumber,
                                                                @Param("currentUserId") Long currentUserId);
}
