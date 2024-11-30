import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ArticleDTO} from '../dtos/article-dto';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private apiUrl = '/api/articles';

  constructor(private http: HttpClient) {
  }

  createArticle(themeId: number, title: string, content: string): Observable<any> {
    const articleData = { themeId: themeId, title, content };
    return this.http.post(this.apiUrl, articleData);
  }

  getArticles(): Observable<ArticleDTO[]> {
    return this.http.get<ArticleDTO[]>(this.apiUrl);
  }

  getArticle(id: string): Observable<ArticleDTO> {
    return this.http.get<ArticleDTO>(`${this.apiUrl}/${id}`);
  }

}
