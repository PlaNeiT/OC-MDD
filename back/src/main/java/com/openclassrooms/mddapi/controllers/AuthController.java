package com.openclassrooms.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.config.JwtUtil;
import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.exceptions.InvalidPasswordException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controller for handling authentication-related requests such as registration, login, and user updates.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user.
     * 
     * @param user the user to be registered.
     * @return a response entity containing the success message and status code.
     */
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a success message.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody User user) {
        try {
            authService.register(user);
            return new ResponseEntity<>(new AuthResponseDTO("User successfully created"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Logs in a user.
     * 
     * @param user the user credentials to log in.
     * @return a response entity containing the authentication response and status code.
     */
    @Operation(summary = "Log in a user", description = "Authenticates a user and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody User user) {
        try {
            AuthResponseDTO response = authService.login(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage(), "Unauthorized", null), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Updates the user's information. The password will only be updated if provided.
     * 
     * @param user the user object containing the updated information.
     * @param token the authorization token used to identify the user.
     * @return a response entity containing the update status and the new token.
     */
    @Operation(
        summary = "Update user information", 
        description = "Updates the information of an authenticated user. A new token is returned after the update.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PutMapping("/update")
    public ResponseEntity<AuthResponseDTO> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        try {
            // Retrieve the current authenticated user's username
            String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
            authService.updateUser(user, currentPrincipalName);

            // Generate a new token after the user information is updated
            String newToken = jwtUtil.generateToken(user.getUsername(), user.getEmail(), user.getId());
            return new ResponseEntity<>(new AuthResponseDTO("User successfully updated", newToken), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
