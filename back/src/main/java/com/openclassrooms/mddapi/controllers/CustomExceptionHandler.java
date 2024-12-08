package com.openclassrooms.mddapi.controllers;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for the application.
 * 
 * This class handles exceptions globally and provides custom responses
 * for specific exception types such as {@link IllegalArgumentException}, {@link BadCredentialsException},
 * {@link AccessDeniedException}, and general {@link Exception}.
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} and {@link IllegalStateException}.
     * <p>
     * Returns a BAD_REQUEST status and logs the exception details.
     * </p>
     * 
     * @param runtimeException The exception that was thrown.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleIllegalArgumentException(RuntimeException runtimeException, WebRequest request) {
        logger.error("Error handling exception: ", runtimeException);
        return super.handleExceptionInternal(runtimeException, getErrorDetails(runtimeException, request),
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles {@link BadCredentialsException}.
     * <p>
     * Returns an UNAUTHORIZED status and logs the exception details.
     * </p>
     * 
     * @param badCredentialsException The exception that was thrown.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { BadCredentialsException.class })
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException badCredentialsException,
                                                                   WebRequest request) {
        logger.error("Unauthorized access attempt: ", badCredentialsException);
        return handleExceptionInternal(badCredentialsException, getErrorDetails(badCredentialsException, request),
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handles {@link AccessDeniedException}.
     * <p>
     * Returns a FORBIDDEN status and logs the exception details.
     * </p>
     * 
     * @param accessDeniedException The exception that was thrown.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> handleForbiddenException(AccessDeniedException accessDeniedException,
                                                              WebRequest request) {
        logger.error("Access denied: ", accessDeniedException);
        return handleExceptionInternal(accessDeniedException, getErrorDetails(accessDeniedException, request),
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * Handles all other {@link Exception} types.
     * <p>
     * Returns an INTERNAL_SERVER_ERROR status and logs the exception details.
     * </p>
     * 
     * @param runtimeException The exception that was thrown.
     * @param request The current web request.
     * @return A {@link ResponseEntity} containing the error details.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleException(RuntimeException runtimeException, WebRequest request) {
        logger.error("Internal server error: ", runtimeException);
        return handleExceptionInternal(runtimeException, "Internal Server error. Try later !!", new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Constructs the error details to be sent in the response.
     * 
     * @param exception The exception that was thrown.
     * @param request The current web request.
     * @return A {@link ErrorDetails} object containing the timestamp, message, and request description.
     */
    private ErrorDetails getErrorDetails(Exception exception, WebRequest request) {
        return new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false));
    }
}
