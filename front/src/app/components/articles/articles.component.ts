import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Router } from "@angular/router";

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {
  articles: any[] = [];
  isAscending: boolean = true;

  constructor(private articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.articleService.getArticles().subscribe(
      (response: any) => {
        this.articles = response;
        this.sortArticles();
      },
      (error) => {
        console.error('Erreur lors du chargement des articles:', error);
      }
    );
  }

  sortArticles(): void {
    this.articles.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return this.isAscending ? dateA - dateB : dateB - dateA;
    });
  }

  toggleSortOrder(): void {
    this.isAscending = !this.isAscending;
    this.sortArticles();
  }

  onArticleClick(articleId: number): void {
    console.log('Article ID:', articleId);
    this.router.navigate([`/articles/${articleId}`]);
  }
}
