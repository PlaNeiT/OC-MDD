import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister() {
    this.authService.register(this.username, this.email, this.password).subscribe(
      (response: any) => {
        console.log('Registration successful', response);
        this.router.navigate(['/login']);
      },
      (error: any) => {
        this.errorMessage = error.error?.error || 'Une erreur est survenue';
        console.error('Registration failed', this.errorMessage);
      }
    );
  }

}
