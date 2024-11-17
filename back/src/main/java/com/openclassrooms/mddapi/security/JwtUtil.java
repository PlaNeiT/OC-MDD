package com.openclassrooms.mddapi.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private final String secretKey = "openclassrooms";
    private final long validity = 365 * 24 * 60 * 60 * 1000;

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

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractEmail(String token) {
        return (String) extractClaims(token).get("email");
    }

    public Long extractUserId(String token) {
        return (Long) extractClaims(token).get("id");
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        try {
            return (extractUsername(token).equals(username) && !isTokenExpired(token));
        } catch (JwtException e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
