import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  identifier: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.identifier, this.password).subscribe(
      (      response: any) => {
        console.log('LOGIN successful', response);
        this.router.navigate(['/login']);
      },
      (      error: any) => {
        this.errorMessage = error.error;
      }
    );
  }
}
