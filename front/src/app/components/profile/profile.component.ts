import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SubscriptionService } from '../../services/subscription.service';
import { ThemeDTO } from '../../dtos/theme-dto';

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
  subscriptions: ThemeDTO[] = []; // Stocke les abonnements de l'utilisateur

  constructor(
    private authService: AuthService,
    private subscriptionService: SubscriptionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
    } else {
      const user = this.authService.getUser();
      if (user) {
        this.username = user.username;
        this.email = user.email;
        this.loadSubscriptions(); // Charger les abonnements lors de l'initialisation
      }
    }
  }

  // Charger les abonnements de l'utilisateur
  loadSubscriptions(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.subscriptionService.getUserSubscriptions(token).subscribe(
        (subscriptions: ThemeDTO[]) => {
          this.subscriptions = subscriptions;
        },
        (error) => {
          console.error('Error loading subscriptions:', error);
        }
      );
    }
  }

  // Sauvegarder le profil de l'utilisateur
  onSaveProfile(): void {
    console.log('User to be updated:', this.username, this.email);
    this.authService.updateUser(this.username, this.email).subscribe(
      (response: any) => {
        console.log('Update success:', response);
        localStorage.setItem('token', response.token);
        this.successMessage = 'Profil mis à jour avec succès';
        this.router.navigate(['/profile']);
      },
      (error) => {
        console.error('Error during update:', error);
        this.errorMessage = error.error?.message || 'Erreur inconnue';
      }
    );
  }

  // Se désabonner du thème
  onUnsubscribe(themeId: number): void {
    this.subscriptionService.unsubscribe(themeId).subscribe(
      (response) => {
        console.log('Unsubscribed:', response);
        // Mettre à jour l'état du thème pour refléter le désabonnement
        const theme = this.subscriptions.find(t => t.id === themeId);
        if (theme) {
          this.subscriptions = this.subscriptions.filter(t => t.id !== themeId); // Retirer l'abonnement de la liste
        }
      },
      (error) => {
        console.error('Error unsubscribing:', error);
      }
    );
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
