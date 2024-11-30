import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CommentDTO} from '../dtos/comment-dto';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = '/api/articles';

  constructor(private http: HttpClient) {
  }

  getCommentsForArticle(articleId: string): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.apiUrl}/${articleId}/comments`);
  }

  addComment(articleId: string, content: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = {Authorization: `Bearer ${token}`};

    return this.http.post(`${this.apiUrl}/${articleId}/comments`, content, {headers});
  }


}
