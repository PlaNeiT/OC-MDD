package com.openclassrooms.mddapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repositories.ThemeRepository;

/**
 * Service class for managing themes in the application.
 * 
 * This service provides functionality to retrieve all themes and transform them into
 * data transfer objects (DTOs) for easier consumption by the client.
 * 
 * Dependencies:
 * - {@link ThemeRepository}: Repository for accessing theme data from the database.
 */
@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    /**
     * Retrieves all themes from the database.
     * 
     * This method fetches all {@link Theme} entities from the database, converts them into 
     * {@link ThemeDTO} objects, and returns a list of these DTOs.
     * 
     * @return a list of {@link ThemeDTO} representing all themes in the system.
     */
    public List<ThemeDTO> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new ThemeDTO(theme.getId(), theme.getName(), theme.getDescription()))
                .collect(Collectors.toList());
    }
}
