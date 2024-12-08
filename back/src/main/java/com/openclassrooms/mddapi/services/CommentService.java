package com.openclassrooms.mddapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.ArticleRepository;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing comments on articles.
 * 
 * This service provides methods to add comments to articles, retrieve comments for a specific article,
 * and fetch user details by username. It interacts with the repositories for data persistence and retrieval.
 * 
 * Dependencies:
 * - {@link CommentRepository}: Repository for managing comment data.
 * - {@link ArticleRepository}: Repository for managing article data.
 * - {@link UserRepository}: Repository for managing user data.
 */
@Service
@Slf4j
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a comment to a specific article by a specific user.
     * 
     * This method fetches the article and user associated with the provided IDs, creates a new
     * comment, and saves it to the database.
     * 
     * @param articleId the ID of the article to which the comment is being added.
     * @param userId the ID of the user adding the comment.
     * @param content the content of the comment.
     * @throws IllegalArgumentException if the article or user is not found.
     */
    public void addComment(Long articleId, Long userId, String content) {
        Comment comment = new Comment();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Set the article, user, and content for the comment
        comment.setArticle(article);
        comment.setUser(user);
        comment.setContent(content);

        // Save the comment
        commentRepository.save(comment);
    }

    /**
     * Retrieves all comments for a specific article.
     * 
     * This method fetches the list of comments associated with the provided article ID.
     * 
     * @param articleId the ID of the article whose comments are to be retrieved.
     * @return a list of {@link Comment} objects associated with the article.
     */
    public List<Comment> getCommentsForArticle(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }

    /**
     * Fetches a user by their username.
     * 
     * This method retrieves a {@link User} object based on the provided username.
     * 
     * @param username the username of the user to fetch.
     * @return the {@link User} object associated with the provided username.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
