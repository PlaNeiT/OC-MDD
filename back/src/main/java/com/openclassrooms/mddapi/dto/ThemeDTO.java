package com.openclassrooms.mddapi.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NonNull;

/**
 * Data Transfer Object (DTO) for themes.
 * 
 * This DTO represents the data structure used to transfer theme information between the API layers.
 * It contains fields for the theme's unique identifier, name, and description, and includes validation 
 * constraints to ensure data integrity.
 * 
 * Annotations:
 * 
 *   {@link lombok.Data} - Automatically generates getters, setters, equals, hashCode, and toString methods.
 *   {@link lombok.NonNull} - Ensures the field cannot be null.
 *   {@link javax.validation.constraints.NotBlank} - Validates that the string fields are not null or empty.
 * 
 * Fields:
 * 
 *   id - The unique identifier for the theme.
 *   name - The name of the theme.
 *   description - A brief description of the theme.
 * 
 * Constructors:
 * 
 *   Parameterized constructor: Initializes the DTO with all fields.
 */
@Data
public class ThemeDTO {

    /**
     * The unique identifier of the theme.
     * 
     * This field is required and cannot be null.
     */
    @NonNull
    private Long id;

    /**
     * The name of the theme.
     * 
     * This field is required and must not be blank. If blank, a validation error will be triggered.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * A brief description of the theme.
     * 
     * This field is required and must not be blank. If blank, a validation error will be triggered.
     */
    @NotBlank(message = "Description is required")
    private String description;

    /**
     * Parameterized constructor.
     * 
     * Initializes a new {@code ThemeDTO} object with the specified values.
     * 
     * @param id          the unique identifier of the theme
     * @param name        the name of the theme
     * @param description a brief description of the theme
     */
    public ThemeDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
