import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { ThemeDTO } from '../../dtos/theme-dto';
import { SubscriptionService } from '../../services/subscription.service';

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.css']
})
export class ThemesComponent implements OnInit {
  themes: ThemeDTO[] = [];

  constructor(private themeService: ThemeService, private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.themeService.getThemes().subscribe(themesDTO => {
      this.themes = themesDTO;
      this.checkSubscriptions();
    });
  }

  checkSubscriptions(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.subscriptionService.getUserSubscriptions(token).subscribe(subscribedThemes => {
        this.themes.forEach(theme => {
          theme.isSubscribed = subscribedThemes.some(subscribedTheme => subscribedTheme.id === theme.id);
        });
      });
    }
  }

  // Abonnement au thème
  onSubscribe(themeId: number): void {
    // Vérification préalable si l'utilisateur est déjà abonné
    const theme = this.themes.find(t => t.id === themeId);
    if (theme && theme.isSubscribed) {
      console.log('Déjà abonné à ce thème');
      return; // Ne fait rien si déjà abonné
    }

    this.subscriptionService.subscribe(themeId).subscribe(response => {
      console.log('Subscribed:', response);
      // Mettez à jour l'état du bouton pour refléter l'abonnement
      const theme = this.themes.find(t => t.id === themeId);
      if (theme) {
        theme.isSubscribed = true; // Mettez à jour l'état local
      }
    }, error => {
      // Si erreur "déjà abonné", on passe juste l'état du bouton à "Se désabonner"
      if (error.error && error.error.message === 'Vous êtes déjà abonné à ce thème.') {
        const theme = this.themes.find(t => t.id === themeId);
        if (theme) {
          theme.isSubscribed = true; // L'utilisateur est déjà abonné, on met à jour l'état
        }
      }
      console.error('Error subscribing:', error);
    });
  }

  // Désabonnement du thème
  onUnsubscribe(themeId: number): void {
    this.subscriptionService.unsubscribe(themeId).subscribe(response => {
      console.log('Unsubscribed:', response);
      const theme = this.themes.find(t => t.id === themeId);
      if (theme) {
        theme.isSubscribed = false; // Mettez à jour l'état local
      }
    }, error => {
      console.error('Error unsubscribing:', error);
    });
  }
}
