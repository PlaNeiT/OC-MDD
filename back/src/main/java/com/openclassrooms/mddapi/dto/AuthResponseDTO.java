package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * 
 * This DTO is used to encapsulate the response details for authentication requests,
 * including a message, a token (if authentication is successful), and an error message (if any).
 * 
 * Annotations:
 * 
 *   {@link lombok.Data} - Generates boilerplate code such as getters, setters, equals, hashCode, and toString methods.
 * 
 * 
 * 
 * Constructors:
 * 
 *   Default constructor: Initializes an empty object.
 *   Constructor with message only: Used to send a message without a token or error.
 *   Constructor with message and token: Used to send a success message and token.
 *   Constructor with message, error, and token: Used to send a message with an error and token, if applicable.
 * 
 * 
 * 
 */
@Data
public class AuthResponseDTO {

    /**
     * A message describing the result of the authentication process.
     */
    private String message;

    /**
     * A token issued upon successful authentication.
     */
    private String token;

    /**
     * An error message describing the issue if authentication fails.
     */
    private String error;

    /**
     * Default constructor.
     * 
     * Initializes an empty {@code AuthResponseDTO} object.
     */
    public AuthResponseDTO() {}

    /**
     * Constructor with a message.
     * 
     * Initializes the {@code AuthResponseDTO} with a message.
     * 
     * @param message the message describing the result of the authentication process
     */
    public AuthResponseDTO(String message) {
        this.message = message;
    }

    /**
     * Constructor with a message and a token.
     * 
     * Initializes the {@code AuthResponseDTO} with a message and a token.
     * 
     * @param message the message describing the result of the authentication process
     * @param token the token issued upon successful authentication
     */
    public AuthResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    /**
     * Constructor with a message, error, and token.
     * 
     * Initializes the {@code AuthResponseDTO} with a message, an error, and a token.
     * 
     * @param message the message describing the result of the authentication process
     * @param error the error message describing the issue if authentication fails
     * @param token the token issued upon successful authentication
     */
    public AuthResponseDTO(String message, String error, String token) {
        this.message = message;
        this.error = error;
        this.token = token;
    }
}
