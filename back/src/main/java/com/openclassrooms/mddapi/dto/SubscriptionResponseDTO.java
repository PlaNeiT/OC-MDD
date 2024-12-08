package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for responses related to subscription operations.
 * 
 * This DTO is used to encapsulate the response message for subscription-related requests.
 * 
 * Annotations:
 * 
 *   {@link lombok.Data} - Automatically generates getters, setters, equals, hashCode, and toString methods for the class.
 * 
 * 
 * 
 * Constructors:
 * 
 *   Constructor with a message: Allows setting a custom response message.
 * 
 * 
 * 
 */
@Data
public class SubscriptionResponseDTO {

    /**
     * A message describing the result of a subscription operation.
     */
    private String message;

    /**
     * Constructor with a message.
     * 
     * Initializes the {@code SubscriptionResponseDTO} with a specific response message.
     * 
     * @param message the response message for the subscription operation
     */
    public SubscriptionResponseDTO(String message) {
        this.message = message;
    }
}
