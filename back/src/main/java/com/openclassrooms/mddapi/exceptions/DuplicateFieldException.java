package com.openclassrooms.mddapi.exceptions;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String message) {
        super(message);
    }
}
