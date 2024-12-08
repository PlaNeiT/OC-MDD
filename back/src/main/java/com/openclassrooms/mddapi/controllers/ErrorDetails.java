package com.openclassrooms.mddapi.controllers;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * ErrorDetails is used to provide detailed information about the error that occurred.
 * It contains the timestamp of the error, a message describing the error, and additional details.
 */
@Data
public class ErrorDetails {

    /** The timestamp when the error occurred */
    private LocalDateTime timestamp;

    /** A message describing the error */
    private String message;

    /** Additional details about the error */
    private String details;

    /**
     * Constructs an ErrorDetails object with the specified timestamp, message, and details.
     * 
     * @param timestamp The timestamp when the error occurred
     * @param message A message describing the error
     * @param details Additional details about the error
     */
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
