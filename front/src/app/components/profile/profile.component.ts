import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  username: string = '';
  email: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
    } else {
      const user = this.authService.getUser();
      if (user) {
        this.username = user.username;
        this.email = user.email;
      }
    }
  }

  onSaveProfile() {
    console.log('User to be updated:', this.username, this.email); // Ajoutez ce log
    this.authService.updateUser(this.username, this.email).subscribe(
      response => {
        console.log('Update success:', response);
        this.router.navigate(['/profile']);
      },
      error => {
        console.error('Error during update:', error);
        this.errorMessage = error.error?.message || 'Erreur inconnue';
      }
    );
  }


  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
