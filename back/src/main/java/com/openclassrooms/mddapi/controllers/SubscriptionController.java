package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubscriptionResponseDTO;
import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.models.Theme;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> subscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        try {
            subscriptionService.subscribeToTheme(themeId, token);
            return new ResponseEntity<>(new SubscriptionResponseDTO("Abonnement réussi"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new SubscriptionResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/unsubscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> unsubscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        try {
            subscriptionService.unsubscribeFromTheme(themeId, token);
            return new ResponseEntity<>(new SubscriptionResponseDTO("Désabonnement réussi"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new SubscriptionResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/subscriptions")
    public List<Theme> getUserSubscriptions(@RequestHeader("Authorization") String token) {
        return subscriptionService.getUserSubscriptions(token);
    }
}
