package org.vitaliistf.cardealership.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user account in the car dealership system.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** The unique identifier of the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** The first name of the user. */
    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @Column(name = "first_name")
    private String firstName;

    /** The last name of the user. */
    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @Column(name = "last_name")
    private String lastName;

    /** The email address of the user. */
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Column(name = "email", unique = true)
    private String email;

    /** The password of the user. */
    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @Column(name = "password")
    private String password;

    /** The phone number of the user. */
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+?\\d{10,13}$", message = "Invalid phone number format.")
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    /** The address of the user. */
    @NotBlank(message = "Address is required.")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters.")
    @Column(name = "address")
    private String address;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
