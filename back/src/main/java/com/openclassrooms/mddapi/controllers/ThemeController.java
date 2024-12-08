package com.openclassrooms.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.services.ThemeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for managing themes.
 * 
 * This controller provides endpoints to access and manage themes in the system.
 * 
 * Base URL: /api/themes
 * 
 * Dependencies:
 * 
 * {@link ThemeService}: Service layer for managing themes.
 */
@Tag(name = "Themes", description = "Operations related to themes")
@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    /**
     * Constructor for {@code ThemeController}.
     * 
     * Initializes the controller with the required {@link ThemeService}.
     * 
     * @param themeService the service layer for managing themes
     */
    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Retrieves all themes.
     * 
     * This endpoint is accessible via a GET request to /api/themes.
     * 
     * @return a list of {@link ThemeDTO} representing all themes
     */
    @Operation(
        summary = "Get all themes",
        description = "Retrieves a list of all themes available in the system."
    )
    @GetMapping
    public List<ThemeDTO> getAllThemes() {
        return themeService.getAllThemes();
    }
}
