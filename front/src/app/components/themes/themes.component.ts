import {Component, OnInit, OnDestroy} from '@angular/core';
import {ThemeService} from '../../services/theme.service';
import {ThemeDTO} from '../../dtos/theme-dto';
import {SubscriptionService} from '../../services/subscription.service';
import {Subscription} from "rxjs";

@Component({
  selector: 'app-themes',
  templateUrl: './themes.component.html',
  styleUrls: ['./themes.component.scss']
})
export class ThemesComponent implements OnInit, OnDestroy {
  themes: ThemeDTO[] = [];
  subscription: Subscription = new Subscription();

  constructor(private themeService: ThemeService, private subscriptionService: SubscriptionService) {
  }

  ngOnInit(): void {
    this.loadThemes();
  }

  loadThemes(): void {
    this.subscription = this.themeService.getThemes().subscribe(themesDTO => {
      this.themes = themesDTO;
      this.checkSubscriptions();
    });
  }

  checkSubscriptions(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.subscription = this.subscriptionService.getUserSubscriptions(token).subscribe(subscribedThemes => {
        this.themes.forEach(theme => {
          theme.isSubscribed = subscribedThemes.some(subscribedTheme => subscribedTheme.id === theme.id);
        });
      });
    }
  }

  onSubscribe(themeId: number): void {
    const theme = this.themes.find(t => t.id === themeId);
    if (theme && theme.isSubscribed) {
      console.log('Déjà abonné à ce thème');
      return;
    }

    this.subscription = this.subscriptionService.subscribe(themeId).subscribe(response => {
      console.log('Subscribed:', response);
      const theme = this.themes.find(t => t.id === themeId);
      if (theme) {
        theme.isSubscribed = true;
      }
    }, error => {
      if (error.error && error.error.message === 'Vous êtes déjà abonné à ce thème.') {
        const theme = this.themes.find(t => t.id === themeId);
        if (theme) {
          theme.isSubscribed = true;
        }
      }
      console.error('Error subscribing:', error);
    });
  }

  onUnsubscribe(themeId: number): void {
    this.subscription = this.subscriptionService.unsubscribe(themeId).subscribe(response => {
      console.log('Unsubscribed:', response);
      const theme = this.themes.find(t => t.id === themeId);
      if (theme) {
        theme.isSubscribed = false;
      }
    }, error => {
      console.error('Error unsubscribing:', error);
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
