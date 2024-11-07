package com.openclassrooms.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.mddapi.models.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
