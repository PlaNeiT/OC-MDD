import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentDTO } from '../dtos/comment-dto';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = '/api/articles';

  constructor(private http: HttpClient) {
  }

  getCommentsForArticle(articleId: number): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.apiUrl}/${articleId}/comments`);
  }

  addComment(articleId: number, content: string): Observable<void> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}`, 'Content-Type': 'text/plain' }; // Set 'Content-Type' for plain text

    return this.http.post<void>(`${this.apiUrl}/${articleId}/comments`, content, { headers });
  }

}
