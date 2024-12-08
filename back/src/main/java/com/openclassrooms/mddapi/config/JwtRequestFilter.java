package com.openclassrooms.mddapi.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * This filter is responsible for intercepting incoming HTTP requests and validating
 * the JWT token provided in the "Authorization" header. If the token is valid, 
 * it authenticates the user and sets the authentication in the security context.
 * 
 * The filter is invoked once per request and processes the request to ensure that
 * the user is authenticated before the request is passed down the filter chain.
 * 
 * It extracts the token from the "Authorization" header, validates the token,
 * and sets the authenticated user in the Spring Security context if the token is valid.
 * 
 * If the token is invalid, expired, or modified, the filter responds with a 401 
 * Unauthorized status and a message indicating the issue.
 * 
 * @see JwtUtil
 * @see UsernamePasswordAuthenticationToken
 */
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * The utility class for handling JWT-related operations such as extraction and validation.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * This method is responsible for filtering incoming HTTP requests. It checks if the request 
     * contains a valid JWT token in the "Authorization" header and authenticates the user if the token is valid.
     * If the token is invalid, expired, or modified, a 401 Unauthorized status is returned to the client.
     * 
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain to pass the request along
     * @throws ServletException if the filter encounters a servlet-specific error
     * @throws IOException if an I/O error occurs during the filter process
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if the Authorization header contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Extract the JWT token
            try {
                // Extract the username from the token
                username = jwtUtil.extractUsername(jwt);
            } catch (JwtException e) {
                // If the token is invalid or modified, return 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalide ou modifié");
                log.error("Erreur dans la validation du token : " + e.getMessage());
                return;
            }
        }

        // If a valid username is extracted and the user is not yet authenticated, validate the token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {
                // Create authentication token for the user and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // If the token is invalid or expired, return 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalide ou expiré");
                return;
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}
