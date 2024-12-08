import {Component, OnInit, OnDestroy} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Router, NavigationEnd} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  isLoggedIn: boolean = false;
  isMenuOpen: boolean = false;
  isMobile: boolean = false;
  subscription: Subscription = new Subscription();

  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.checkMobile();
    window.addEventListener('resize', this.checkMobile.bind(this));

    const previousRoute = localStorage.getItem('previousRoute');
    if (previousRoute) {
      this.router.navigateByUrl(previousRoute);
    }

    this.subscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        if (event.url) {
          localStorage.setItem('previousRoute', event.url);
        }
      }
    });
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  checkMobile(): void {
    this.isMobile = window.innerWidth <= 600;
  }

  navigateToArticles(): void {
    this.router.navigate(['/articles']);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
