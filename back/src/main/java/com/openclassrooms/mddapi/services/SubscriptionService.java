package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.exceptions.ThemeNotFoundException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

/**
 * Service class for managing user subscriptions to themes.
 * 
 * This service provides functionality to subscribe or unsubscribe a user to a theme,
 * as well as retrieve all themes to which a user is subscribed. It interacts with 
 * repositories for data persistence and retrieval.
 * 
 * Dependencies:
 * - {@link ThemeRepository}: Repository for managing theme data.
 * - {@link UserRepository}: Repository for managing user data.
 * - {@link SubscriptionRepository}: Repository for managing subscription data.
 */
@Service
public class SubscriptionService {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Subscribes a user to a specific theme.
     * 
     * This method checks if the user is already subscribed to the theme. If not, it creates a 
     * new subscription and saves it to the database.
     * 
     * @param themeId the ID of the theme to subscribe to.
     * @param username the username of the user subscribing to the theme.
     * @throws ThemeNotFoundException if the theme with the given ID does not exist.
     * @throws IllegalArgumentException if the user is already subscribed to the theme.
     */
    public void subscribeToTheme(Long themeId, String username) {
        User user = userRepository.findByUsername(username);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found"));

        if (subscriptionRepository.existsByUserIdAndThemeId(user.getId(), theme.getId())) {
            throw new IllegalArgumentException("You are already subscribed to this theme.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscriptionRepository.save(subscription);
    }

    /**
     * Unsubscribes a user from a specific theme.
     * 
     * This method checks if the subscription exists and removes it from the database.
     * 
     * @param themeId the ID of the theme to unsubscribe from.
     * @param username the username of the user unsubscribing from the theme.
     * @throws ThemeNotFoundException if the theme with the given ID does not exist.
     * @throws IllegalArgumentException if the user is not subscribed to the theme.
     */
    public void unsubscribeFromTheme(Long themeId, String username) {
        User user = userRepository.findByUsername(username);
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Theme not found"));

        Subscription subscription = subscriptionRepository.findByUserIdAndThemeId(user.getId(), theme.getId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        subscriptionRepository.delete(subscription);
    }

    /**
     * Retrieves all themes a user is subscribed to.
     * 
     * This method fetches a list of themes the user is currently subscribed to using the user's ID.
     * 
     * @param username the username of the user whose subscriptions are to be retrieved.
     * @return a list of {@link Theme} objects representing the user's subscriptions.
     */
    public List<Theme> getUserSubscriptions(String username) {
        User user = userRepository.findByUsername(username);
        return subscriptionRepository.findThemesByUserId(user.getId());
    }
}
