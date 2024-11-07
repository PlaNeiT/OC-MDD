package com.openclassrooms.mddapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<Map<String, String>> register(User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            response.put("error", "Nom d'utilisateur ou e-mail existe déjà");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        response.put("message", "Utilisateur créé avec succès");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<Map<String, String>> login(User user) {
        Map<String, String> response = new HashMap<>();

        // Rechercher l'utilisateur par nom d'utilisateur
        User userFoundByUsername = userRepository.findByUsername(user.getUsername());
        if (userFoundByUsername != null) {
            if (passwordEncoder.matches(user.getPassword(), userFoundByUsername.getPassword())) {
                response.put("message", "Connexion réussie");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("error", "Mot de passe incorrect");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        }

        // Rechercher l'utilisateur par e-mail si non trouvé par nom d'utilisateur
        User userFoundByEmail = userRepository.findByEmail(user.getEmail());
        if (userFoundByEmail != null) {
            if (passwordEncoder.matches(user.getPassword(), userFoundByEmail.getPassword())) {
                response.put("message", "Connexion réussie");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("error", "Mot de passe incorrect");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        }

        response.put("error", "Identifiant non trouvé");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
