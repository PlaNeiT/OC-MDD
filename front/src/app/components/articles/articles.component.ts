import { Component, OnInit, OnDestroy } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Router } from "@angular/router";
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit, OnDestroy {
  articles: any[] = [];
  isAscending: boolean = true;
  subscription: Subscription = new Subscription();

  constructor(private articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.subscription = this.articleService.getArticles().subscribe(
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
    this.router.navigate([`/articles/${articleId}`]);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
