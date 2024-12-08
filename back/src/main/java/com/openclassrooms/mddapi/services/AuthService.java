package com.openclassrooms.mddapi.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.config.JwtUtil;
import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.exceptions.InvalidPasswordException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;

/**
 * Service class for authentication and user management.
 * 
 * This service handles user registration, login, and profile updates. It ensures that
 * user data is valid, manages password encryption, and generates JWT tokens for authentication.
 * 
 * Constants:
 * - {@code EMAIL_REGEX} - Regex for validating email format.
 * - {@code PASSWORD_REGEX} - Regex for validating password strength.
 * 
 * Dependencies:
 * - {@link UserRepository} - Repository for user data management.
 * - {@link PasswordEncoder} - Encrypts user passwords.
 * - {@link JwtUtil} - Generates and validates JWT tokens.
 */
@Service
public class AuthService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user.
     * 
     * Validates the user's email and password format, checks for duplicate usernames or emails,
     * encrypts the password, and saves the user to the database.
     * 
     * @param user the {@link User} object containing the user's information.
     * @throws IllegalArgumentException if email, password, username, or email already exists.
     */
    public void register(User user) {
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("The email is not valid");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter, and one special character");
        }

        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Username or email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Logs in a user.
     * 
     * Validates the user's credentials by checking the username/email and password. Generates a JWT token upon successful authentication.
     * 
     * @param user the {@link User} object containing the user's credentials.
     * @return an {@link AuthResponseDTO} containing the login message and JWT token.
     * @throws UserNotFoundException if the username or email is not found.
     * @throws InvalidPasswordException if the password is incorrect.
     */
    public AuthResponseDTO login(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null) {
            foundUser = userRepository.findByEmail(user.getUsername());
        }

        if (foundUser == null) {
            throw new UserNotFoundException("Identifier not found");
        }

        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId());
        return new AuthResponseDTO("Login successful", token);
    }

    /**
     * Updates the user's information.
     * 
     * Allows the user to update their username, email, or password. Validates new email and password formats,
     * checks for duplicate usernames or emails, and encrypts the password if updated.
     * 
     * @param user the {@link User} object containing the updated information.
     * @param username the current username of the user making the request.
     * @throws UserNotFoundException if the user is not found.
     * @throws IllegalArgumentException if the new username or email already exists or if the email/password is invalid.
     */
    public void updateUser(User user, String username) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Username already taken");
            }
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
                throw new IllegalArgumentException("The email is not valid");
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("This email is already in use");
            }
            existingUser.setEmail(user.getEmail());
        }

        // Update the password if provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (!isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Password must contain at least 8 characters, one digit, one lowercase letter, one uppercase letter, and one special character");
            }
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }

    /**
     * Validates the password format.
     * 
     * Checks whether the password meets the required complexity defined in {@code PASSWORD_REGEX}.
     * 
     * @param password the password to validate.
     * @return {@code true} if the password is valid, otherwise {@code false}.
     */
    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
