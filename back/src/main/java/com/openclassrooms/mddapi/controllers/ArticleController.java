package com.openclassrooms.mddapi.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.ArticleRequestDTO;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller to manage Articles and their related operations.
 * Handles CRUD operations for articles, including creating new articles, 
 * retrieving articles, and managing comments associated with them.
 */
@Slf4j
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    /**
     * Endpoint to create a new article.
     * 
     * @param articleRequest The DTO containing the article data to be created.
     * @param token The JWT token for authenticating the user.
     * @return A ResponseEntity containing the created article.
     */
    @Operation(
        summary = "Create a new article",
        description = "Allows an authenticated user to create a new article.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping
    public ResponseEntity<Article> createArticle(
            @RequestBody @Valid ArticleRequestDTO articleRequest, 
            @RequestHeader("Authorization") String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setContent(articleRequest.getContent());

        User user = articleService.getUserByUsername(currentPrincipalName);
        article.setUser(user);
        article.setTheme(articleService.getThemeById(articleRequest.getThemeId()));

        Article savedArticle = articleService.createArticle(article);

        return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all articles.
     * 
     * @return A list of all articles.
     */
    @Operation(summary = "Get all articles", description = "Retrieves a list of all articles.")
    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    /**
     * Endpoint to get a specific article by its ID.
     * 
     * @param id The ID of the article to retrieve.
     * @return The article with the specified ID.
     */
    @Operation(summary = "Get an article by ID", description = "Retrieves an article by its ID.")
    @GetMapping("/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    /**
     * Endpoint to get all comments associated with an article.
     * 
     * @param id The ID of the article for which comments are to be retrieved.
     * @return A list of comments associated with the article.
     */
    @Operation(summary = "Get comments for an article", description = "Retrieves all comments for a given article.")
    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsForArticle(@PathVariable Long id) {
        return commentService.getCommentsForArticle(id);
    }

    /**
     * Endpoint to add a comment to a specific article.
     * 
     * @param id The ID of the article to which the comment will be added.
     * @param content The content of the comment to be added.
     * @param token The JWT token for authenticating the user.
     */
    @Operation(
        summary = "Add a comment to an article",
        description = "Allows an authenticated user to add a comment to an article.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PostMapping("/{id}/comments")
    public void addComment(
            @PathVariable Long id,
            @RequestBody String content,
            @RequestHeader("Authorization") String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user = commentService.getUserByUsername(currentPrincipalName);
        commentService.addComment(id, user.getId(), content);
    }
}
