package com.openclassrooms.mddapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration class for the MDD application.
 * <p>
 * This class configures Spring Security for the application, defining the security rules for various endpoints,
 * as well as specifying the password encoding strategy and security filter chain.
 * </p>
 * 
 * <p>
 * The security configuration:
 * <ul>
 *   <li>Disables CSRF protection (not recommended for production unless needed for APIs).</li>
 *   <li>Allows public access to authentication endpoints for registration and login.</li>
 *   <li>Requires authentication for theme, subscription, and article-related endpoints.</li>
 *   <li>Uses a custom filter, {@link JwtRequestFilter}, to intercept and process JWT tokens before reaching
 *       the {@link UsernamePasswordAuthenticationFilter}.</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    /**
     * The JWT request filter that intercepts incoming requests to validate JWT tokens.
     */
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Bean for password encoder.
     * 
     * This method provides a BCryptPasswordEncoder to securely hash passwords before storage or comparison.
     * 
     * @return a {@link PasswordEncoder} configured with BCrypt algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for the security filter chain configuration.
     * 
     * This method defines the security rules for HTTP requests, including which routes are accessible to authenticated users
     * and which are publicly accessible. It also adds a custom filter for JWT token validation before Spring's default
     * authentication filter.
     * 
     *
     * @param http the {@link HttpSecurity} object used to configure web security
     * @return a {@link SecurityFilterChain} object containing the configured HTTP security rules
     * @throws Exception if the configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
            ).permitAll()
            .antMatchers("/api/auth/register", "/api/auth/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
