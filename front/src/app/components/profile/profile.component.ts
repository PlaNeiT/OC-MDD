import { Component, OnInit, OnDestroy } from '@angular/core';
import { SubscriptionService } from '../../services/subscription.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ThemeDTO } from '../../dtos/theme-dto';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit, OnDestroy {
  username: string = '';
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  subscriptions: ThemeDTO[] = [];
  subscription: Subscription = new Subscription();

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
        this.loadSubscriptions();
      }
    }
  }

  loadSubscriptions(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.subscription = this.subscriptionService.getUserSubscriptions(token).subscribe(
        (subscriptions: ThemeDTO[]) => {
          this.subscriptions = subscriptions;
        },
        (error) => {
          console.error('Error loading subscriptions:', error);
        }
      );
    }
  }

  onSaveProfile(): void {
    this.subscription = this.authService.updateUser(this.username, this.email, this.password).subscribe(
      (response: any) => {
        localStorage.setItem('token', response.token);
        this.successMessage = 'Profil mis à jour avec succès';
        this.router.navigate(['/profile']);
      },
      (error) => {
        this.errorMessage = error.error?.message || 'Erreur inconnue';
      }
    );
  }


  onUnsubscribe(themeId: number): void {
    this.subscription = this.subscriptionService.unsubscribe(themeId).subscribe(response => {
      console.log('Unsubscribed:', response);
      this.subscriptions = this.subscriptions.filter(t => t.id !== themeId);
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
