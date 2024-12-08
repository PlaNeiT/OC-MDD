import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthDto} from '../dtos/auth-dto';
import {UserDto} from '../dtos/user-dto';
import {UpdateUserDto} from '../dtos/updateUser-dto';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';

  constructor(private http: HttpClient, private router: Router) {
  }

  register(username: string, email: string, password: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(`${this.apiUrl}/register`, { username, email, password });
  }

  login(usernameOrEmail: string, password: string): Observable<AuthDto> {
    return this.http.post<AuthDto>(`${this.apiUrl}/login`, { username: usernameOrEmail, password });
  }

  updateUser(username: string, email: string, password: string): Observable<UserDto> {
    const updatedUser: UpdateUserDto = {
      username: username,
      email: email,
      password: password || null,
    };
    return this.http.put<UserDto>(`${this.apiUrl}/update`, updatedUser);
  }

  getUser(): UserDto | null {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {username: payload.sub, email: payload.email, id: payload.id};
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
      this.router.navigate(['/articles']); // Si token valide
    } else if (token && !this.isLoggedIn()) {
      this.router.navigate(['/login']); // Si token expiré ou modifié
    }
    // Si pas de token, ne redirige pas, laisser l'utilisateur se connecter
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
