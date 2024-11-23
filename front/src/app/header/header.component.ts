import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  isMenuOpen: boolean = false;
  isMobile: boolean = false;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.isLoggedIn = this.authService.isLoggedIn();
    this.checkMobile();
    window.addEventListener('resize', this.checkMobile.bind(this));

    const previousRoute = localStorage.getItem('previousRoute');
    if (previousRoute) {
      this.router.navigateByUrl(previousRoute);
    }

    this.router.events.subscribe((event: any) => {
      if (event.url) {
        localStorage.setItem('previousRoute', event.url);
      }
    });
  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  checkMobile() {
    this.isMobile = window.innerWidth <= 600;
  }

  navigateToArticles() {
    this.router.navigate(['/articles']);
  }
}
