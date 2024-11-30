import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {Subscription} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  identifier: string = '';
  password: string = '';
  errorMessage: string = '';
  subscription: Subscription = new Subscription();

  constructor(private authService: AuthService, private router: Router) {
  }

  onLogin() {
    this.subscription = this.authService.login(this.identifier, this.password).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.token);
        this.router.navigate(['/articles']);
      },
      (error) => {
        this.errorMessage = error.error?.message || 'Erreur inconnue';
      }
    );
  }
}
