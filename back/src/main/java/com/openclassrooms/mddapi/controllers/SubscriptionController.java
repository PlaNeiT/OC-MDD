package com.openclassrooms.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.config.JwtUtil;
import com.openclassrooms.mddapi.dto.SubscriptionResponseDTO;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.services.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/subscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> subscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7)); // Retire le "Bearer "
        subscriptionService.subscribeToTheme(themeId, usernameFromToken);
        return new ResponseEntity<>(new SubscriptionResponseDTO("Abonnement réussi"), HttpStatus.OK);
    }

    @PostMapping("/unsubscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> unsubscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7)); // Retire le "Bearer "
        subscriptionService.unsubscribeFromTheme(themeId, usernameFromToken);
        return new ResponseEntity<>(new SubscriptionResponseDTO("Désabonnement réussi"), HttpStatus.OK);
    }

    @GetMapping("/user/subscriptions")
    public List<Theme> getUserSubscriptions(@RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7)); // Retire le "Bearer "
        return subscriptionService.getUserSubscriptions(usernameFromToken);
    }
}
