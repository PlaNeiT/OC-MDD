import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private router: Router) {
  }

  register(username: string, email: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, {username, email, password});
  }

  login(usernameOrEmail: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, {username: usernameOrEmail, password});
  }

  updateUser(username: string, email: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = {Authorization: `Bearer ${token}`};
    return this.http.put(`${this.apiUrl}/update`, {username, email}, {headers});
  }

  getUser() {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return { username: payload.sub, email: payload.email };
    }
    return null;
  }

  // Validation du token (vérification de sa signature et de son expiration)
  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    if (!token) {
      return false;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1])); // Extraire le payload
      const expiry = payload.exp * 1000; // Date d'expiration du token
      if (expiry > Date.now()) {
        return true;  // Token valide
      } else {
        this.logout();  // Token expiré
        return false;
      }
    } catch (e) {
      this.logout(); // Erreur dans le décodage ou token modifié
      return false;
    }
  }

  checkTokenValidityAndRedirect(): void {
    const token = localStorage.getItem('token');
    if (token && this.isLoggedIn()) {
      this.router.navigate(['/profile']); // Si token valide
    } else if (token && !this.isLoggedIn()) {
      this.router.navigate(['/login']); // Si token expiré ou modifié
    }
    // Si pas de token, ne redirige pas, laisser l'utilisateur se connecter
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
