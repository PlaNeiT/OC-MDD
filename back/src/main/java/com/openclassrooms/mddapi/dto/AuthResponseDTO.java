package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String message;
    private String token;
    private String error;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String message) {
        this.message = message;
    }

    public AuthResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public AuthResponseDTO(String message, String error, String token) {
        this.message = message;
        this.error = error;
        this.token = token;
    }
}
