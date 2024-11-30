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

@Service
public class SubscriptionService {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;


    public void subscribeToTheme(Long themeId, String username) {
        User user = userRepository.findByUsername(username);
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new ThemeNotFoundException("Thème non trouvé"));

        if (subscriptionRepository.existsByUserIdAndThemeId(user.getId(), theme.getId())) {
            throw new IllegalArgumentException("Vous êtes déjà abonné à ce thème.");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribeFromTheme(Long themeId, String username) {
        User user = userRepository.findByUsername(username);
        Theme theme = themeRepository.findById(themeId).orElseThrow(() -> new ThemeNotFoundException("Thème non trouvé"));

        Subscription subscription = subscriptionRepository.findByUserIdAndThemeId(user.getId(), theme.getId())
                .orElseThrow(() -> new IllegalArgumentException("Abonnement non trouvé"));

        subscriptionRepository.delete(subscription);
    }

    public List<Theme> getUserSubscriptions(String username) {
        User user = userRepository.findByUsername(username);
        return subscriptionRepository.findThemesByUserId(user.getId());
    }
}
