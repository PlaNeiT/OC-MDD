package com.openclassrooms.mddapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s.theme FROM Subscription s WHERE s.user.id = :userId")
    List<Theme> findThemesByUserId(@Param("userId") Long userId);

    // Vérifie si l'utilisateur est déjà abonné à un thème
    boolean existsByUserIdAndThemeId(Long userId, Long themeId);

    // Retourne une souscription par utilisateur et thème
    Optional<Subscription> findByUserIdAndThemeId(Long userId, Long themeId);
}

