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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * REST controller for managing user subscriptions to themes.
 * 
 * This controller provides endpoints for subscribing to and unsubscribing from themes,
 * as well as retrieving a list of themes that the user is subscribed to.
 * 
 * It uses JWT authentication to identify the user making the request.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Subscribe the user to a theme.
     * 
     * @param themeId the ID of the theme to subscribe to
     * @param token the JWT token containing the user's authentication information
     * @return a response indicating the success of the subscription
     */
    @Operation(
        summary = "Subscribe to a theme",
        description = "Allows the authenticated user to subscribe to a specific theme by providing the theme ID.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/subscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> subscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));
        subscriptionService.subscribeToTheme(themeId, usernameFromToken);
        return new ResponseEntity<>(new SubscriptionResponseDTO("Subscription successful"), HttpStatus.OK);
    }

    /**
     * Unsubscribe the user from a theme.
     * 
     * @param themeId the ID of the theme to unsubscribe from
     * @param token the JWT token containing the user's authentication information
     * @return a response indicating the success of the unsubscription
     */
    @Operation(
        summary = "Unsubscribe from a theme",
        description = "Allows the authenticated user to unsubscribe from a specific theme by providing the theme ID.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/unsubscribe/{themeId}")
    public ResponseEntity<SubscriptionResponseDTO> unsubscribe(@PathVariable Long themeId, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));
        subscriptionService.unsubscribeFromTheme(themeId, usernameFromToken);
        return new ResponseEntity<>(new SubscriptionResponseDTO("Unsubscription successful"), HttpStatus.OK);
    }

    /**
     * Get a list of themes that the user is subscribed to.
     * 
     * @param token the JWT token containing the user's authentication information
     * @return a list of themes the user is subscribed to
     */
    @Operation(
        summary = "Get user subscriptions",
        description = "Retrieves a list of themes the authenticated user is subscribed to.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @GetMapping("/user/subscriptions")
    public List<Theme> getUserSubscriptions(@RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));
        return subscriptionService.getUserSubscriptions(usernameFromToken);
    }
}
