package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.ArticleRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing articles.
 * 
 * This service provides methods to handle CRUD operations for articles, as well as
 * utility methods to retrieve associated themes and users. It acts as the intermediary
 * between the controllers and repositories, implementing business logic and ensuring 
 * transactional consistency.
 * 
 * Annotations:
 * 
 *   {@link org.springframework.stereotype.Service} - Marks this class as a service bean.
 *   {@link lombok.extern.slf4j.Slf4j} - Provides logging capabilities using SLF4J.
 *   {@link org.springframework.transaction.annotation.Transactional} - Ensures transactional 
 *   consistency for database operations.
 * 
 * Dependencies:
 * 
 *   {@link ArticleRepository} - Repository for managing {@link Article} entities.
 *   {@link ThemeRepository} - Repository for managing {@link Theme} entities.
 *   {@link UserRepository} - Repository for managing {@link User} entities.
 */
@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new article.
     * 
     * @param article the {@link Article} object to be saved.
     * @return the saved {@link Article}.
     */
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    /**
     * Retrieves all articles.
     * 
     * @return a list of all {@link Article} entities.
     */
    @Transactional(readOnly = true)
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    /**
     * Retrieves an article by its ID.
     * 
     * @param id the ID of the article to retrieve.
     * @return the {@link Article} with the specified ID.
     * @throws RuntimeException if the article is not found.
     */
    @Transactional(readOnly = true)
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    /**
     * Retrieves a user by their username.
     * 
     * @param username the username of the user to retrieve.
     * @return the {@link User} with the specified username.
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves a theme by its ID.
     * 
     * @param themeId the ID of the theme to retrieve.
     * @return the {@link Theme} with the specified ID.
     * @throws RuntimeException if the theme is not found.
     */
    @Transactional(readOnly = true)
    public Theme getThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Theme not found"));
    }
}
