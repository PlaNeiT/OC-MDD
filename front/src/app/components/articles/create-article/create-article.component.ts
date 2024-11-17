import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../../services/article.service';
import { Router } from '@angular/router';
import { ThemeDTO } from '../../../dtos/theme-dto';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {
  title: string = '';
  content: string = '';
  selectedThemeId: number | null = null;
  themes: ThemeDTO[] = [];

  constructor(
    private articleService: ArticleService,
    private themeService: ThemeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.themeService.getThemes().subscribe(
      (themes: ThemeDTO[]) => {
        this.themes = themes;
      },
      (error) => {
        console.error('Error loading themes:', error);
      }
    );
  }

  onCreateArticle(): void {
    if (this.selectedThemeId && this.title && this.content) {
      const token = localStorage.getItem('token');
      if (token) {
        this.articleService.createArticle(
          this.selectedThemeId,
          this.title,
          this.content,
          token
        ).subscribe(
          (response) => {
            console.log('Article created successfully', response);
            this.router.navigate(['/articles']);
          },
          (error) => {
            console.error('Error creating article:', error);
          }
        );
      }
    }
  }
}
