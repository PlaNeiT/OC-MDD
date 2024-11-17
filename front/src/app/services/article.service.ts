import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ArticleDTO } from '../dtos/article-dto';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = 'http://localhost:8080/api/articles';

  constructor(private http: HttpClient) {}

  createArticle(themeId: number, title: string, content: string, token: string): Observable<any> {
    const headers = { Authorization: `Bearer ${token}` };
    const articleData = { themeId, title, content };
    return this.http.post(this.apiUrl, articleData, { headers });
  }

  getArticles(): Observable<ArticleDTO[]> {
    return this.http.get<ArticleDTO[]>(this.apiUrl);
  }
}
