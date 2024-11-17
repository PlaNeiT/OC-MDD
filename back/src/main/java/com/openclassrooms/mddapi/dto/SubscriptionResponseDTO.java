package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionResponseDTO {
    private String message;

    public SubscriptionResponseDTO(String message) {
        this.message = message;
    }
}

