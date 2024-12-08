import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {Subscription} from "rxjs";

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
  subscription: Subscription = new Subscription();

  constructor(private authService: AuthService, private router: Router) {
  }

  onRegister(): void {
    // Validation du format de l'email
    const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
    if (!emailRegex.test(this.email)) {
      this.errorMessage = "L'email n'est pas valide";
      return;
    }

    // Nettoyer les espaces
    this.username = this.username.trim();
    this.email = this.email.trim();
    this.password = this.password.trim();

    // Validation du mot de passe
    const passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$/;
    if (!passwordRegex.test(this.password)) {
      this.errorMessage = "Le mot de passe doit contenir au moins 8 caractères, un chiffre, une lettre minuscule, une majuscule et un caractère spécial";
      return;
    }

    this.subscription = this.authService.register(this.username, this.email, this.password).subscribe(
      response => {
        console.log('User registered successfully:', response);
        this.router.navigate(['/login']);
      },
      error => {
        this.errorMessage = error.error?.error;
      }
    );
  }
}
