import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  register(username: string, email: string, password: string): Observable<any> {
    const user = { username, email, password };
    return this.http.post(`/api/auth/register`, user);
  }

  login(email: string, password: string): Observable<any> {
    const user = { email, password };
    return this.http.post(`/api/auth/login`, user);
  }
}
