package com.openclassrooms.mddapi.services;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.exceptions.InvalidPasswordException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Expression régulière pour valider l'email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Expression régulière pour valider le mot de passe
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$";

    public void register(User user) {
        // Validation de l'email
        if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("L'email n'est pas valide");
        }

        // Validation du mot de passe
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères, un chiffre, une lettre minuscule, une majuscule et un caractère spécial");
        }

        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Nom d'utilisateur ou e-mail existe déjà");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public AuthResponseDTO login(User user) {
        // Essayer de chercher par nom d'utilisateur
        User foundUser = userRepository.findByUsername(user.getUsername());

        if (foundUser == null) {
            // Si aucun utilisateur trouvé par nom d'utilisateur, chercher par email
            foundUser = userRepository.findByEmail(user.getUsername());
        }

        if (foundUser == null) {
            // Si l'utilisateur n'est pas trouvé, lever une exception
            logger.error("Identifiant non trouvé pour l'utilisateur : {}", user.getUsername());
            throw new UserNotFoundException("Identifiant non trouvé");
        }

        // Vérification du mot de passe
        if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            logger.error("Mot de passe incorrect pour l'utilisateur : {}", user.getUsername());
            throw new InvalidPasswordException("Mot de passe incorrect");
        }

        // Si tout est OK, générer le token
        String token = jwtUtil.generateToken(foundUser.getUsername());
        logger.info("Connexion réussie pour l'utilisateur : {}", foundUser.getUsername());

        return new AuthResponseDTO("Login successful", token);
    }



    public void updateUser(User user, String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7)); // Retire "Bearer "
        logger.info("Tentative de mise à jour pour l'utilisateur : {}", usernameFromToken); // Ajoutez ce log

        User existingUser = userRepository.findByUsername(usernameFromToken);

        if (existingUser == null) {
            logger.error("Utilisateur non trouvé pour le nom d'utilisateur : {}", usernameFromToken); // Ajoutez ce log
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        if (user.getUsername() != null) {
            // Vérification que le username n'est pas déjà pris
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new IllegalArgumentException("Nom d'utilisateur déjà pris");
            }
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            // Vérification de la validité de l'email et s'il est déjà utilisé
            if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
                throw new IllegalArgumentException("L'email n'est pas valide");
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Cet e-mail est déjà utilisé");
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getPassword() != null) {
            if (!isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères, un chiffre, une lettre minuscule, une majuscule et un caractère spécial");
            }
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(existingUser);
    }


    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }
}
