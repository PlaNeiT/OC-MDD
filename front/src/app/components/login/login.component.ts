import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  identifier: string = ''; // Cela peut être un username ou un email
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.identifier, this.password).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.token); // Stockage du token dans le localStorage
        this.router.navigate(['/profile']);
      },
      (error) => {
        this.errorMessage = error.error?.message || 'Erreur inconnue';
      }
    );
  }
}
