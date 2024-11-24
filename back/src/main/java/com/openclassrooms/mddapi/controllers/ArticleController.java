package com.openclassrooms.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.CommentService;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.JwtUtil;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody ArticleRequestDTO articleRequest, @RequestHeader("Authorization") String token) {
        try {
            Article article = articleService.createArticle(articleRequest, token);
            return new ResponseEntity<>(article, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsForArticle(@PathVariable Long id) {
        return commentService.getCommentsForArticle(id);
    }

    @PostMapping("/{id}/comments")
    public void addComment(@PathVariable Long id, @RequestBody String content, @RequestHeader("Authorization") String token) {
        String usernameFromToken = jwtUtil.extractUsername(token.substring(7));

        User user = userRepository.findByUsername(usernameFromToken);
        if (user == null) {
            throw new UserNotFoundException("Utilisateur non trouvé");
        }

        Long userId = user.getId();

        commentService.addComment(id, userId, content);
    }

}
