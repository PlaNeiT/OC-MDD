package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for handling article creation requests.
 * 
 * This DTO is used to validate and transfer data from the client when creating a new article.
 * 
 * Validation Constraints:
 * 
 *   {@code themeId} - Must not be null.
 *   {@code title} - Must not be blank and must have a length between 3 and 100 characters.
 *   {@code content} - Must not be blank.
 * 
 * 
 * 
 * Annotations:
 * 
 *   {@link javax.validation.constraints.NotNull} - Ensures non-null fields.
 *   {@link javax.validation.constraints.NotBlank} - Ensures non-empty and non-blank fields.
 *   {@link javax.validation.constraints.Size} - Enforces size constraints.
 * 
 * 
 * 
 * Uses Lombok's {@code @Data} annotation to generate boilerplate code such as getters, setters, and toString methods.
 * 
 */
@Data
public class ArticleRequestDTO {

    /**
     * The ID of the theme associated with the article.
     * 
     * Validation:
     * 
     *   Must not be null.
     * 
     * 
     * 
     * @see javax.validation.constraints.NotNull
     */
    @NotNull(message = "Theme ID is required")
    private Long themeId;

    /**
     * The title of the article.
     * 
     * Validation:
     * 
     *   Must not be blank.
     *   Must have a length between 3 and 100 characters.
     * 
     * 
     * 
     * @see javax.validation.constraints.NotBlank
     * @see javax.validation.constraints.Size
     */
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    /**
     * The content of the article.
     * 
     * Validation:
     * 
     *   Must not be blank.
     * 
     * 
     * 
     * @see javax.validation.constraints.NotBlank
     */
    @NotBlank(message = "Content is required")
    private String content;
}
