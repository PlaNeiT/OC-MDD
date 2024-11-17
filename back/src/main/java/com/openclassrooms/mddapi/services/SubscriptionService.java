package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.exceptions.ThemeNotFoundException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;

@Service
public class SubscriptionService {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public void subscribeToTheme(Long themeId, String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7)); // Retire le "Bearer "
        User user = userRepository.findByUsername(usernameFromToken);
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new ThemeNotFoundException("Thème non trouvé"));

        // Vérification si l'utilisateur est déjà abonné
        if (subscriptionRepository.existsByUserIdAndThemeId(user.getId(), theme.getId())) {
            // Si déjà abonné, ne rien faire et retourner une réponse appropriée
            throw new IllegalArgumentException("Vous êtes déjà abonné à ce thème.");
        }

        // Créer un abonnement
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromTheme(Long themeId, String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(usernameFromToken);
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new ThemeNotFoundException("Thème non trouvé"));

        Subscription subscription = subscriptionRepository.findByUserIdAndThemeId(user.getId(), theme.getId())
                .orElseThrow(() -> new IllegalArgumentException("Abonnement non trouvé"));

        subscriptionRepository.delete(subscription);
    }

    public List<Theme> getUserSubscriptions(String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));
        User user = userRepository.findByUsername(usernameFromToken);
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        return subscriptionRepository.findThemesByUserId(user.getId());
    }



}

