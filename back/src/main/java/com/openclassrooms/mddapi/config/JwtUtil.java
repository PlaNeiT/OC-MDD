package com.openclassrooms.mddapi.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Utility class for generating and validating JSON Web Tokens (JWT).
 * This class contains methods for creating a JWT, extracting claims,
 * validating tokens, and extracting specific information like the
 * username, email, and user ID from a token.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing the JWT. 
     * It should be kept secure and not hardcoded in production.
     */
    private final String secretKey = "openclassrooms";

    /**
     * Validity period of the token in milliseconds (365 days).
     */
    private final long validity = 365 * 24 * 60 * 60 * 1000;

    /**
     * Generates a JWT token with the specified user information.
     * 
     * @param username the username of the user
     * @param email the email of the user
     * @param userId the ID of the user
     * @return the generated JWT token as a String
     */
    public String generateToken(String username, String email, Long userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Extracts the claims from a JWT token.
     * 
     * @param token the JWT token
     * @return the claims from the JWT token
     * @throws JwtException if the token is invalid or cannot be parsed
     */
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            System.out.println("Token parsing failed: " + e.getMessage());
            throw new JwtException("Token invalide ou corrompu.");
        }
    }

    /**
     * Extracts the username from the JWT token.
     * 
     * @param token the JWT token
     * @return the username stored in the token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts the email from the JWT token.
     * 
     * @param token the JWT token
     * @return the email stored in the token
     */
    public String extractEmail(String token) {
        return (String) extractClaims(token).get("email");
    }

    /**
     * Extracts the user ID from the JWT token.
     * 
     * @param token the JWT token
     * @return the user ID stored in the token
     */
    public Long extractUserId(String token) {
        return (Long) extractClaims(token).get("id");
    }

    /**
     * Checks if the JWT token is expired.
     * 
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Validates the JWT token by checking if the username matches and if the token is not expired.
     * 
     * @param token the JWT token
     * @param username the username to compare with the token's username
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, String username) {
        try {
            return (extractUsername(token).equals(username) && !isTokenExpired(token));
        } catch (JwtException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
