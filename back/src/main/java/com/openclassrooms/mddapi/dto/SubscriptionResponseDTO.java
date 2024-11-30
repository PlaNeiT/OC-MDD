package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class SubscriptionResponseDTO {
    private String message;

    public SubscriptionResponseDTO(String message) {
        this.message = message;
    }
}