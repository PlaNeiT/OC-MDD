package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.services.AuthService;
import com.openclassrooms.mddapi.config.JwtUtil;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.exceptions.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody User user) {
        try {
            authService.register(user);
            return new ResponseEntity<>(new AuthResponseDTO("Utilisateur créé avec succès"), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

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

    @PutMapping("/update")
    public ResponseEntity<AuthResponseDTO> updateUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        try {
            String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
            authService.updateUser(user, currentPrincipalName);

            String newToken = jwtUtil.generateToken(user.getUsername(), user.getEmail(), user.getId());
            return new ResponseEntity<>(new AuthResponseDTO("Utilisateur mis à jour avec succès", newToken), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new AuthResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
