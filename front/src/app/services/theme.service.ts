import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ThemeDTO} from '../dtos/theme-dto';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private apiUrl = '/api/themes';

  constructor(private http: HttpClient) {
  }

  getThemes(): Observable<ThemeDTO[]> {
    return this.http.get<ThemeDTO[]>(this.apiUrl);
  }
}
