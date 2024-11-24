package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDTO {
    private Long themeId;
    private String title;
    private String content;
}
