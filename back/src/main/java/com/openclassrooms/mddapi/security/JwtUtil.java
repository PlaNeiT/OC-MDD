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
    // 1 an
    private final long validity = 365 * 24 * 60 * 60 * 1000;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
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
            // Log l'exception pour aider au débogage
            System.out.println("Token parsing failed: " + e.getMessage());
            throw new JwtException("Token invalide ou corrompu.");
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        try {
            return (extractUsername(token).equals(username) && !isTokenExpired(token));
        } catch (JwtException e) {
            // Retourne false si le token est modifié, expiré ou invalide
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}

